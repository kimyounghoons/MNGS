package com.mngs.kimyounghoon.mngs.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.AGREEMENT
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.AGREEMENT_URL
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.PRIVACY_POLICY
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.PRIVACY_POLICY_URL
import com.mngs.kimyounghoon.mngs.databinding.FragmentLoginBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupProgressDialog
import java.util.*
import java.util.regex.Pattern

class LoginFragment : AbstractFragment() {

    override fun getTitle(): String {
        return getString(R.string.login)
    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN: Int = 10
    private lateinit var callbackManager: CallbackManager
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private var viewModel: LoginViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        fragmentLoginBinding = FragmentLoginBinding.bind(root).apply {
            this@LoginFragment.viewModel = obtainViewModel()
            viewModel = this@LoginFragment.viewModel
            this.signupExplainText.apply {
                val transform = Linkify.TransformFilter { match, url -> EMPTY }
                val agreeTermsPattern = Pattern.compile(AGREEMENT)
                val privacyPolicyPattern = Pattern.compile(PRIVACY_POLICY)
                Linkify.addLinks(this, agreeTermsPattern, AGREEMENT_URL, null, transform)
                Linkify.addLinks(this, privacyPolicyPattern, PRIVACY_POLICY_URL, null, transform)
            }
        }
        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.apply {
            view.setupProgressDialog(this@LoginFragment, needProgress)
            onFacebookLoginCommand.observe(this@LoginFragment, Observer {
                LoginManager.getInstance().logInWithReadPermissions(this@LoginFragment, Arrays.asList("public_profile"))
            })
            onGoogleLoginCommand.observe(this@LoginFragment, Observer {
                signIn()
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showActionBar()
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                viewModel?.dismissProgress()
            }

            override fun onError(error: FacebookException) {
                viewModel?.dismissProgress()
            }
        })

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        var credential: AuthCredential = FacebookAuthProvider.getCredential(token.getToken())
        auth?.signInWithCredential(credential)?.addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful) {
                viewModel?.tryHome(auth?.uid!!, locateListener)
            } else {
                viewModel?.dismissProgress()
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {
                viewModel?.dismissProgress()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        val user = auth?.currentUser
                        viewModel?.tryHome(auth?.uid!!, locateListener)
                    } else {
                        viewModel?.dismissProgress()
                        Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun obtainViewModel(): LoginViewModel = obtainViewModel(LoginViewModel::class.java)

}