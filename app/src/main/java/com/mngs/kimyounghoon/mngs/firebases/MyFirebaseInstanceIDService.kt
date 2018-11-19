package com.mngs.kimyounghoon.mngs.firebases

import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceIDService : FirebaseMessagingService(){
    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

    }
}