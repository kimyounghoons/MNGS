package com.mngs.kimyounghoon.mngs.firebases

import com.google.firebase.messaging.FirebaseMessagingService
import com.mngs.kimyounghoon.mngs.data.source.LettersFirebaseDataSource

class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRefreshToken(token)
    }

    private fun sendRefreshToken(token: String) {
        token.apply {
            LettersFirebaseDataSource.sendRefreshToken(this)
        }
    }
}