package com.mngs.kimyounghoon.mngs

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var mAuth: FirebaseAuth? = null
    private val RC_SIGN_IN: Int = 10
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var emailLoginButton: Button
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailEditText = findViewById(R.id.edittext_email)
        passwordEditText = findViewById(R.id.edittext_password)
        emailLoginButton = findViewById(R.id.email_login_button)
        val info: PackageInfo
        info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        for (signature: Signature in info.signatures) {

            var md: MessageDigest = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            var something = String(Base64.encode(md.digest(), 0));
            //String something = new String(Base64.encodeBytes(md.digest()));
            Log.d("hash key", something);
        }
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        var googleLoginButton: SignInButton = findViewById(R.id.google_login_button)
        googleLoginButton.setOnClickListener {
            signIn()
        }

        emailLoginButton.setOnClickListener {
            loginUser(emailEditText.text.toString(), passwordEditText.text.toString())
//            createUser(emailEditText.text.toString(), passwordEditText.text.toString())
        }

        callbackManager = CallbackManager.Factory.create();
        val loginButton = findViewById(R.id.facebook_login_button) as LoginButton
        loginButton.setReadPermissions("email")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel()
            {
                // App code
            }

            override fun onError(error : FacebookException)
            {
                // App code
            }
        });

    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        var credential: AuthCredential = FacebookAuthProvider.getCredential(token.getToken())
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
            override fun onComplete(task: Task<AuthResult>) {
                if (task.isSuccessful) {
                    Log.d("MainActivity", "연동 성공")
                } else {
                    Log.d("MainActivity", "연동 실패")
                }
            }
        })
    }

    private fun createUser(email: String, password: String) {  // 회원가입
        mAuth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth?.currentUser
                        Toast.makeText(this, "success",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }
    }

    private fun loginUser(email: String, password: String) {  // 로그인 방법
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "로그인 성공",
                            Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "로그인 실패",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this, { task ->

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()
                        val user = mAuth?.getCurrentUser()
                        Toast.makeText(this, user.toString(), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
                    }

                })
    }

}
