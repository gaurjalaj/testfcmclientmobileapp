package com.demoapppoc

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.demoapppoc.utils.DBHelper
import com.demoapppoc.utils.EncryptedSharedPrefWrapper
import com.demoapppoc.utils.Helpers
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.provider.FirebaseInitProvider
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.util.UUID


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "MyFirebaseMessagingService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken --> $token")
        try {
            val pref = EncryptedSharedPrefWrapper().getEncryptedSharedPreferenceInstance(applicationContext)
            EncryptedSharedPrefWrapper().put(pref, Constants.fcmToken, token)
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "(onMessageReceived) remoteMessage: ${remoteMessage.data}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
//            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // Check if data needs to be processed by long running job
//            if (needsToBeScheduled()) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                handleNow()
//            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        acknowledgeServer(remoteMessage);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Create a notification channel for Android O and above
        val channelId = "demo_channel_id"
        val channelName = "DemoChannelName"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = "description"
            notificationManager.createNotificationChannel(channel)
//        }

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.sym_def_app_icon) // replace with your app icon
            .setContentTitle("Notification Title") // Set a proper title
            .setContentText(messageBody) // Set the message body
            .setAutoCancel(true) // Make the notification dismissible
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(UUID.randomUUID().hashCode(), notificationBuilder.build())
    }

    private fun acknowledgeServer(remoteMessage: RemoteMessage) {
        try {
            writeDataInLocalDB(remoteMessage)
        }catch (ex: Exception){
            ex.printStackTrace();
        }
//        Log.d(TAG, "(acknowledgeServer) remoteMessage: ${remoteMessage.data}")
        val apiClient = OkHttpClient();
        val request = Request.Builder()
//                .url("https://jsonplaceholder.typicode.com/todos/1")
                .url("http://192.168.1.12:3000/acknowlege/"+remoteMessage.data["notificationId"])
                .build()
        apiClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                Log.d(TAG, "onResponse---> ${e.toString()}")
                sendNotification("New Notification from Server")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d(TAG, "onResponse---> $response")
                    sendNotification("New Notification from Server")
                    // Process the response body
                } else {
                    sendNotification("New Notification from Server")
                    // Handle unsuccessful response
                }
            }
        })
    }

    private fun writeDataInLocalDB(remoteMessage: RemoteMessage){
        DBHelper(applicationContext).insertData(remoteMessage.data.toString())
    }
}