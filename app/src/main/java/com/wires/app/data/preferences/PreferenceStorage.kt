package com.wires.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

class PreferenceStorage @Inject constructor(
    val context: Context
) {

    companion object {
        private const val SECURE_PREF_FILE_NAME = "secured_preferences"
        private const val ACCESS_TOKEN_KEY = "access_token_key"
    }

    private val securePref: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        securePref = EncryptedSharedPreferences.create(
            context,
            SECURE_PREF_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var accessToken: String?
        get() = securePref.getString(ACCESS_TOKEN_KEY, null)
        set(value) = securePref.edit().putString(ACCESS_TOKEN_KEY, value).apply()

}