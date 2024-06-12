package com.demoapppoc.utils

import android.content.Context
import android.nfc.Tag
import android.util.Log
import com.demoapppoc.Constants
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class Helpers {
    companion object {
        fun getFcmToken(context: Context): String? {
            try {
                val pref =
                        EncryptedSharedPrefWrapper().getEncryptedSharedPreferenceInstance(context)
                return EncryptedSharedPrefWrapper().get(pref, Constants.fcmToken)
            } catch (ex: Exception) {
                throw ex
            }
        }

        fun acknowledgeServerFor1Minute() {
            for (i in 1..100) {
                val apiClient = OkHttpClient()
                val request = Request.Builder()
                        .url("http://192.168.0.218:3000/acknowlege/" + "sampleReq65456464")
                        .build()
                apiClient.newCall(request).enqueue(object : Callback {

                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("acknowledgeServerFor1Minute()->onFailure()-->", e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.d("acknowledgeServerFor1Minute()->onResponse()-->", response.toString())
                    }

                })
            }
        }
    }
}