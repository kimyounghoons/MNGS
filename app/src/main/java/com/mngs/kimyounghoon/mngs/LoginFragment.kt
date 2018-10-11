package com.mngs.kimyounghoon.mngs

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.google.firebase.auth.*
import com.mngs.kimyounghoon.mngs.databinding.FragmentLoginBinding
import java.util.*

class LoginFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onClick(v: View?) {
        if (v == fragmentLoginBinding.googleLoginButton) {
            signIn()
        } else if (v == fragmentLoginBinding.facebookLoginButton) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var mAuth: FirebaseAuth? = null
    private val RC_SIGN_IN: Int = 10
    private lateinit var callbackManager: CallbackManager
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private var locateListener: LocateListener? = null

    override fun onStart() {
        super.onStart()
        mAuth?.addAuthStateListener(mAuthListener)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LocateListener) {
            locateListener = context
        }
    }

    override fun onStop() {
        super.onStop()
        mAuth?.removeAuthStateListener { mAuthListener }
    }

    override fun onDetach() {
        super.onDetach()
        locateListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        fragmentLoginBinding.googleLoginButton.setOnClickListener(this)
        fragmentLoginBinding.facebookLoginButton.setOnClickListener(this)
        return fragmentLoginBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

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

        mAuthListener = FirebaseAuth.AuthStateListener {
            val user: FirebaseUser? = it.currentUser
            if (user != null) {
                locateToHome()
            } else {
            }
        }

    }

    private fun locateToHome() {
       locateListener?.openHome()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        var credential: AuthCredential = FacebookAuthProvider.getCredential(token.getToken())
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful) {
                locateToHome()
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
        if (requestCode === RC_SIGN_IN) {
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
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(context, "성공", Toast.LENGTH_LONG).show()
                        val user = mAuth?.getCurrentUser()
                        Toast.makeText(context, user.toString(), Toast.LENGTH_LONG).show()
                        locateToHome()
                    } else {
                        Toast.makeText(context, "실패", Toast.LENGTH_LONG).show()
                    }

                }
    }

}
