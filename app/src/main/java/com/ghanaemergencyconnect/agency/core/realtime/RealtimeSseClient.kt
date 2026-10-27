package com.ghanaemergencyconnect.agency.core.realtime

import com.ghanaemergencyconnect.agency.BuildConfig
import com.ghanaemergencyconnect.agency.core.security.SecureSessionManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class RealtimeSseClient(
    private val sessionManager: SecureSessionManager
) {
    private var eventSource: EventSource? = null

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    fun connect() {
        val accessToken = sessionManager.getAccessToken() ?: return
        if (eventSource != null) return

        val request = Request.Builder()
            .url(BuildConfig.API_BASE_URL + "api/v1/staff/incidents/stream")
            .header("Authorization", "Bearer $accessToken")
            .header("Accept", "text/event-stream")
            .build()

        val factory = EventSources.createFactory(okHttpClient)
        eventSource = factory.newEventSource(request, object : EventSourceListener() {
            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                val eventName = type ?: "GENERIC"
                val reportId = parseJsonField(data, "reportId")
                val assignmentId = parseJsonField(data, "assignmentId")

                RealtimeInvalidationBus.emitEvent(
                    RealtimeEvent(
                        name = eventName,
                        reportId = reportId,
                        assignmentId = assignmentId,
                        message = data
                    )
                )
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                disconnect()
            }
        })
    }

    fun disconnect() {
        eventSource?.cancel()
        eventSource = null
    }

    private fun parseJsonField(jsonStr: String, field: String): String? {
        return try {
            val obj = JSONObject(jsonStr)
            obj.optString(field, null)
        } catch (e: Exception) {
            null
        }
    }
}
