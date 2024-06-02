package com.demoapppoc.utils

import android.content.Context
import com.demoapppoc.Constants

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
    }
}