package com.mngs.kimyounghoon.mngs

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    lateinit var nameTextView: TextView
    lateinit var emailTextView: TextView
    lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        nameTextView = findViewById(R.id.name_text)
        emailTextView = findViewById(R.id.email_text)
        logoutButton = findViewById(R.id.logout_button)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        nameTextView.text = auth.currentUser?.displayName
        emailTextView.text = auth.currentUser?.email
        logoutButton.setOnClickListener {
            auth.signOut()
            LoginManager.getInstance().logOut()
            finish()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
