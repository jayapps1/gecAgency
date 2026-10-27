package com.ghanaemergencyconnect.agency.core.navigation

import com.ghanaemergencyconnect.agency.data.domain.model.StaffRole

/**
 * Navigation visibility improves UX but does not grant authorization;
 * backend access control remains authoritative for every operational request.
 */
object RoleNavigationPolicy {

    fun isAllowed(role: StaffRole, destination: AppDestination): Boolean {
        return when (role) {
            StaffRole.AGENCY_ADMIN,
            StaffRole.REGIONAL_ADMIN,
            StaffRole.STATION_ADMIN,
            StaffRole.CONTROL_ROOM_OPERATOR,
            StaffRole.RESPONDER -> true

            StaffRole.SYSTEM_ADMIN,
            StaffRole.REPORT_REVIEWER,
            StaffRole.AUDITOR -> destination in setOf(
                AppDestination.Splash,
                AppDestination.Login,
                AppDestination.Dashboard,
                AppDestination.Profile,
                AppDestination.Settings,
                AppDestination.Notifications
            )

            StaffRole.UNKNOWN -> destination in setOf(
                AppDestination.Splash,
                AppDestination.Login,
                AppDestination.Dashboard,
                AppDestination.Profile,
                AppDestination.Settings
            )
        }
    }

    fun getBottomNavItems(role: StaffRole): List<AppDestination> {
        return when (role) {
            StaffRole.AGENCY_ADMIN,
            StaffRole.REGIONAL_ADMIN,
            StaffRole.STATION_ADMIN,
            StaffRole.CONTROL_ROOM_OPERATOR,
            StaffRole.RESPONDER -> listOf(
                AppDestination.Dashboard,
                AppDestination.Incidents,
                AppDestination.Notifications,
                AppDestination.Profile
            )

            else -> listOf(
                AppDestination.Dashboard,
                AppDestination.Profile,
                AppDestination.Settings
            )
        }
    }
}
