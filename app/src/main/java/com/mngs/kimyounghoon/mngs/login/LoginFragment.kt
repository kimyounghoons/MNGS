package com.mngs.kimyounghoon.mngs.login

import android.content.Intent
import android.os.Bundle
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
import com.google.firebase.database.*
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentLoginBinding
import java.util.*

class LoginFragment : AbstractFragment(), LoginNavigator {
    override fun getTitle(): String {
        return getString(R.string.login)
    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
    }

    override fun onGoogleLogin() {
        signIn()
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN: Int = 10
    private lateinit var callbackManager: CallbackManager
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onStart() {
        super.onStart()
        auth?.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        auth?.removeAuthStateListener { mAuthListener }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        fragmentLoginBinding = FragmentLoginBinding.bind(root).apply {
            fragment = this@LoginFragment
        }
        return fragmentLoginBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

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
                tryHome()
            } else {
            }
        }

    }

    private fun tryHome() {
        databaseReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "request cancelled", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(auth!!.currentUser!!.uid).exists()) {
                    locateListener?.openHome()
                } else {
                    locateListener?.openSignup()
                }
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
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth?.getCurrentUser()
                        tryHome()
                    } else {
                        Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show()
                    }

                }
    }

}
