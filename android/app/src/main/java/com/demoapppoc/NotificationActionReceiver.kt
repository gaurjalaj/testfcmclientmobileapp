package com.demoapppoc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.demoapppoc.utils.NotificationsUtil

class NotificationActionReceiver : BroadcastReceiver() {
    val TAG = "NotificationActionReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "ACTION_ACCEPT") {
            Log.d(TAG, "Accepted");
            val notificationId = intent.getIntExtra("notificationId", -1)
            if(notificationId != -1){
                NotificationsUtil.clearNotification(context, notificationId)
            }
            // Handle the accept action
    //                Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show()
        }
        else if (intent.action == "ACTION_REJECT") {
            Log.d(TAG, "Rejected");
            val notificationId = intent.getIntExtra("notificationId", -1)
            if(notificationId != -1){
                NotificationsUtil.clearNotification(context, notificationId)
            }
            // Handle the reject action
    //                Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show()
        }
    }
}
