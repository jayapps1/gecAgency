package com.ghanaemergencyconnect.agency.data.domain.model

enum class ApplianceStatus(val label: String) {
    AVAILABLE("Available"),
    DISPATCHED("Dispatched"),
    OUT_OF_COMMISSION("Out of Commission"),
    MAINTENANCE("Maintenance"),
    UNKNOWN("Unknown Status");

    companion object {
        fun fromCode(code: String?): ApplianceStatus {
            if (code.isNullOrBlank()) return UNKNOWN
            val upper = code.trim().uppercase()
            return when {
                upper == "AVAILABLE" -> AVAILABLE
                upper == "DISPATCHED" || upper == "EN_ROUTE" || upper == "ON_SCENE" || upper == "RETURNING" -> DISPATCHED
                upper == "OUT_OF_COMMISSION" || upper == "TEMPORARILY_UNAVAILABLE" -> OUT_OF_COMMISSION
                upper == "MAINTENANCE" -> MAINTENANCE
                else -> UNKNOWN
            }
        }
    }
}
