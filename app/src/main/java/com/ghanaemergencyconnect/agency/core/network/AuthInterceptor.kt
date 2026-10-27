package com.ghanaemergencyconnect.agency.core.network

import com.ghanaemergencyconnect.agency.core.security.SecureSessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Attaches Authorization Bearer token header to outgoing network calls if present.
 */
class AuthInterceptor(
    private val sessionManager: SecureSessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = sessionManager.getAccessToken()

        val request = if (!accessToken.isNullOrBlank() && originalRequest.header("Authorization") == null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}
