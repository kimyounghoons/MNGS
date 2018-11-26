package com.mngs.kimyounghoon.mngs.firebases

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mngs.kimyounghoon.mngs.MainActivity
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.JSON_ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.JSON_LETTER

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.apply {
            val intent = Intent(applicationContext, MainActivity::class.java)
            remoteMessage.data?.apply {
                intent.putExtra(JSON_LETTER, get(JSON_LETTER))
                intent.putExtra(JSON_ANSWER, get(JSON_ANSWER))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(getString(R.string.app_name) + getString(R.string.priority_high), getString(R.string.app_name) + getString(R.string.priority_high), NotificationManager.IMPORTANCE_HIGH)
                channel.description = getString(R.string.app_name) + getString(R.string.priority_high)
                channel.enableLights(true)
                channel.lightColor = Color.BLUE
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(100, 200, 100, 200)
                channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(channel)

                //IMPORTANCE
                //NotificationManager.IMPORTANCE_HIGH :소리 및 팝업 (헤드업 알림)
                //NotificationManager.IMPORTANCE_DEFAULT : 소리
                //NotificationManager.IMPORTANCE_LOW : 소리 없음
                //NotificationManager.IMPORTANCE_MIN : 소리 및 시각적 알림 없음
            }

            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = NotificationCompat.Builder(applicationContext, getString(R.string.app_name) + getString(R.string.priority_high))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)  // 잠금 화면 시 알림

            notificationManager.notify(1, builder.build())

        }
    }

    override fun onMessageSent(p0: String?) {
        super.onMessageSent(p0)
    }
}