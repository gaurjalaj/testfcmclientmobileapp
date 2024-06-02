package com.demoapppoc.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException


class EncryptedSharedPrefWrapper {
    @Throws(GeneralSecurityException::class, IOException::class)
    fun getEncryptedSharedPreferenceInstance(context: Context?): SharedPreferences {
        val masterKey = MasterKey.Builder(context!!)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            "demo-shared-prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun put(pref: SharedPreferences, key: String?, data: String?) {
        val editor = pref.edit()
        editor.putString(key, data)
        editor.apply()
    }

    fun get(
        pref: SharedPreferences,
        key: String?
    ): String? {
        return pref.getString(key, null)
    }

    fun delete(pref: SharedPreferences, key: String?) {
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }
}