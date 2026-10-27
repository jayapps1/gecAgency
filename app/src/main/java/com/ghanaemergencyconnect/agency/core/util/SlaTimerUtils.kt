package com.ghanaemergencyconnect.agency.core.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SlaTimerUtils {

    fun formatElapsedTime(createdAtString: String): String {
        if (createdAtString.isBlank()) return "00:00"
        return try {
            val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
            val createdTime = LocalDateTime.parse(createdAtString.take(19), isoFormatter)
            val now = LocalDateTime.now()
            val duration = Duration.between(createdTime, now)
            val minutes = duration.toMinutes().coerceAtLeast(0)
            val seconds = (duration.seconds % 60).coerceAtLeast(0)
            String.format("%02d:%02d", minutes, seconds)
        } catch (e: Exception) {
            "00:00"
        }
    }

    fun getSlaBadgeLabel(slaState: String?): String {
        return when (slaState?.uppercase()) {
            "ON_TIME" -> "On Time"
            "ATTENTION" -> "Attention"
            "DELAYED" -> "Delayed"
            else -> "On Time"
        }
    }
}
