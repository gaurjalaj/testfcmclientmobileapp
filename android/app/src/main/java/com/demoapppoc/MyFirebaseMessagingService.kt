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
import com.demoapppoc.utils.DBHelper
import com.demoapppoc.utils.EncryptedSharedPrefWrapper
import com.demoapppoc.utils.NotificationsUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException


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
                .url("http://192.168.0.210:3000/acknowlege/"+remoteMessage.data["notificationId"])
                .build()
        apiClient.newCall(request).enqueue(object : Callback {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                Log.d(TAG, "onResponse---> ${e.toString()}")
                NotificationsUtil.sendNotification(applicationContext, remoteMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d(TAG, "onResponse---> $response")
                   NotificationsUtil.sendNotification(applicationContext, remoteMessage)
                    // Process the response body
                } else {
                   NotificationsUtil.sendNotification(applicationContext, remoteMessage)
                    // Handle unsuccessful response
                }
            }
        })
    }

    private fun writeDataInLocalDB(remoteMessage: RemoteMessage){
        DBHelper(applicationContext).insertData(remoteMessage.data.toString())
    }
}