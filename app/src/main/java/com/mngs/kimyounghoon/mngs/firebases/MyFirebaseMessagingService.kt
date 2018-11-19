package com.mngs.kimyounghoon.mngs.firebases

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("파이어베이스 메세징", "Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.apply {
            Log.d("파이어베이스 메세징", "Message Notification Body: " + remoteMessage.notification!!.body)
        }
    }
}