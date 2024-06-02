package com.demoapppoc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class NotificationActionReceiver : BroadcastReceiver() {
    val TAG = "NotificationActionReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_ACCEPT" -> {
                Log.d(TAG, "Accepted");
                // Handle the accept action
//                Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show()
            }
            "ACTION_REJECT" -> {
                Log.d(TAG, "Rejected");
                // Handle the reject action
//                Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
