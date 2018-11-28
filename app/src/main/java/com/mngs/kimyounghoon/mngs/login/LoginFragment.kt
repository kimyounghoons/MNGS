package com.mngs.kimyounghoon.mngs.login

import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
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
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.AGREEMENT_URL
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.PRIVACY_POLICY_URL
import com.mngs.kimyounghoon.mngs.data.User
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersFirebaseDataSource
import com.mngs.kimyounghoon.mngs.databinding.FragmentLoginBinding
import java.util.*
import java.util.regex.Pattern

class LoginFragment : AbstractFragment(), LoginNavigator {

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
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private lateinit var reference: LettersDataSource

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        fragmentLoginBinding = FragmentLoginBinding.bind(root).apply {
            fragment = this@LoginFragment
            this.signupExplainText.apply {
                val transform = Linkify.TransformFilter { match, url -> EMPTY }
                val agreeTermsPattern = Pattern.compile("이용약관")
                val privacyPolicyPattern = Pattern.compile("개인정보 보호정책")
                Linkify.addLinks(this, agreeTermsPattern, AGREEMENT_URL, null, transform)
                Linkify.addLinks(this, privacyPolicyPattern, PRIVACY_POLICY_URL, null, transform)
            }
        }
        return fragmentLoginBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showActionBar()
        auth = FirebaseAuth.getInstance()
        reference = LettersFirebaseDataSource

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("LoginFragment", "FacebookCallback is Success")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("LoginFragment", "FacebookCallback is Canceled")
            }

            override fun onError(error: FacebookException) {
                Log.d("LoginFragment", "FacebookCallback is Error")
            }
        })

    }

    private fun tryHome() {
        reference.getUser(auth?.uid!!, object : LettersDataSource.UserCallback {
            override fun onSuccess(user: User) {
                locateListener?.openHome()
            }

            override fun onFail() {
                val lettersDataSource: LettersDataSource = LettersFirebaseDataSource
                lettersDataSource.signup(object : LettersDataSource.SignupCallback {
                    override fun onSuccess() {
                        //가입 성공
                        locateListener?.openHome()
                    }

                    override fun onFail() {
                        //가입 실패
                    }

                })
            }

        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        var credential: AuthCredential = FacebookAuthProvider.getCredential(token.getToken())
        auth?.signInWithCredential(credential)?.addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful) {
                tryHome()
                Log.d("MainActivity", "연동 성공")
            } else {
                Log.d("MainActivity", "연동 실패")
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
                Log.d("LoginFragment", "google login fail")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        val user = auth?.currentUser
                        tryHome()
                    } else {
                        Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show()
                    }

                }
    }

    override fun onFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
    }

    override fun onGoogleLogin() {
        signIn()
    }

}