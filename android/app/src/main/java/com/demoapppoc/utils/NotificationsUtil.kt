package com.demoapppoc.utils

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.demoapppoc.MainActivity
import com.demoapppoc.NotificationActionReceiver
import com.google.firebase.messaging.RemoteMessage

object NotificationsUtil {

    fun clearAllNotifications(context: Context){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun clearNotification(context: Context, id: Int){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(context: Context, remoteMessage: RemoteMessage) {
        val notifyId = remoteMessage.data["notificationId"].hashCode()

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context, notifyId, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Create intents for accept and reject actions
        val acceptIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "ACTION_ACCEPT"
            putExtra("notificationId", notifyId)
        }
        val acceptPendingIntent = PendingIntent.getBroadcast(context, notifyId+1, acceptIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val rejectIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "ACTION_REJECT"
            putExtra("notificationId", notifyId)
        }
        val rejectPendingIntent = PendingIntent.getBroadcast(context, notifyId+2, rejectIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // Create a notification channel for Android O and above
        val channelId = "demo_channel_id"
        val channelName = "DemoChannelName"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.description = "description"
        notificationManager.createNotificationChannel(channel)
//        }

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.sym_def_app_icon) // replace with your app icon
            .setContentTitle(remoteMessage.data["title"]) // Set a proper title
            .setContentText(remoteMessage.data["body"]) // Set the message body
            .setAutoCancel(true) // Make the notification dismissible
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(0, "Accept", acceptPendingIntent)
            .addAction(0, "Reject", rejectPendingIntent)
        notificationManager.notify(notifyId, notificationBuilder.build())
    }

}