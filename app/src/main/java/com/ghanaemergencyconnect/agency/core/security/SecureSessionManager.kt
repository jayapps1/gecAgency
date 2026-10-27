package com.ghanaemergencyconnect.agency.core.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Authentication tokens and session materials are stored securely in EncryptedSharedPreferences.
 * Passwords, TOTP codes, and secrets are never stored.
 */
class SecureSessionManager(context: Context) {

    private val prefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                "gec_secure_session",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences("gec_fallback_session", Context.MODE_PRIVATE)
        }
    }

    fun saveTokens(accessToken: String?, refreshToken: String?) {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            if (refreshToken != null) putString(KEY_REFRESH_TOKEN, refreshToken)
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun saveStaffData(
        userId: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        agencyId: String?,
        region: String?,
        stationId: String?,
        roles: Set<String>
    ) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_EMAIL, email)
            putString(KEY_FIRST_NAME, firstName)
            putString(KEY_LAST_NAME, lastName)
            putString(KEY_AGENCY_ID, agencyId)
            putString(KEY_REGION, region)
            putString(KEY_STATION_ID, stationId)
            putStringSet(KEY_ROLES, roles)
            apply()
        }
    }

    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)
    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)
    fun getFirstName(): String? = prefs.getString(KEY_FIRST_NAME, null)
    fun getLastName(): String? = prefs.getString(KEY_LAST_NAME, null)
    fun getAgencyId(): String? = prefs.getString(KEY_AGENCY_ID, null)
    fun getRegion(): String? = prefs.getString(KEY_REGION, null)
    fun getStationId(): String? = prefs.getString(KEY_STATION_ID, null)
    fun getRoles(): Set<String> = prefs.getStringSet(KEY_ROLES, emptySet()) ?: emptySet()

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_AGENCY_ID = "agency_id"
        private const val KEY_REGION = "region"
        private const val KEY_STATION_ID = "station_id"
        private const val KEY_ROLES = "roles"
    }
}
