package com.mngs.kimyounghoon.mngs.firebases

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.mngs.kimyounghoon.mngs.data.source.LettersFirebaseDataSource

@SuppressLint("Registered")
class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        sendRefreshToken(token)
    }

    private fun sendRefreshToken(token: String?) {
        token?.apply {
            LettersFirebaseDataSource.sendRefreshToken(this)
        }
    }
}