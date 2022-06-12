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
        private const val PREF_FILE_NAME = "preferences"
        private const val SECURE_PREF_FILE_NAME = "secured_preferences"
        private const val ACCESS_TOKEN_KEY = "access_token_key"
        private const val REFRESH_TOKEN_KEY = "refresh_token_key"
        private const val PUSH_TOKEN_KEY = "push_token_key"
        private const val NEED_UPDATE_PUSH_TOKEN_KEY = "need_update_push_token_key"
    }

    private val pref: SharedPreferences
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
        pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    var accessToken: String?
        get() = securePref.getString(ACCESS_TOKEN_KEY, null)
        set(value) = securePref.edit().putString(ACCESS_TOKEN_KEY, value).apply()

    var refreshToken: String?
        get() = securePref.getString(REFRESH_TOKEN_KEY, null)
        set(value) = securePref.edit().putString(REFRESH_TOKEN_KEY, value).apply()

    var pushToken: String?
        get() = pref.getString(PUSH_TOKEN_KEY, null)
        set(value) = pref.edit().putString(PUSH_TOKEN_KEY, value).apply()

    var needUpdatePushToken: Boolean
        get() = pref.getBoolean(NEED_UPDATE_PUSH_TOKEN_KEY, false)
        set(value) = pref.edit().putBoolean(NEED_UPDATE_PUSH_TOKEN_KEY, value).apply()

    fun clearStorage() {
        pref.edit().clear().apply()
        securePref.edit().clear().apply()
    }
}
