package com.mngs.kimyounghoon.mngs.firebases

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mngs.kimyounghoon.mngs.MainActivity
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.JSON_ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.JSON_LETTER

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a notification payload.
        remoteMessage.notification?.apply {
            val bitmapIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val intent = Intent(applicationContext, MainActivity::class.java)
            remoteMessage.data?.apply {
                intent.putExtra(JSON_LETTER, get(JSON_LETTER))
                intent.putExtra(JSON_ANSWER, get(JSON_ANSWER))
            }

            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = NotificationCompat.Builder(applicationContext).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .setLargeIcon(bitmapIcon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel("NoWorries", "NoWorries", NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "answers"
                channel.enableLights(true)
                channel.lightColor = Color.BLUE
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(100, 200, 100, 200)
                channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
                notificationManager.createNotificationChannel(channel)

            } else {
                val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(applicationContext)
                notificationManager.notify(remoteMessage.messageId?.toInt()?:0, builder.build())
            }

            Log.d("파이어베이스 메세징", "Message Notification Body: " + remoteMessage.notification!!.body)
        }
    }

    override fun onMessageSent(p0: String?) {
        super.onMessageSent(p0)
    }
}