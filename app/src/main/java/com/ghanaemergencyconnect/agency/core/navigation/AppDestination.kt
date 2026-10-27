package com.ghanaemergencyconnect.agency.core.navigation

/**
 * Type-safe central destination definitions for the application navigation graph.
 * Route strings are decoupled from user-facing display text.
 */
sealed interface AppDestination {
    val route: String

    data object Splash : AppDestination {
        override val route: String = "splash"
    }

    data object Login : AppDestination {
        override val route: String = "login"
    }

    data object Dashboard : AppDestination {
        override val route: String = "dashboard"
    }

    data object Incidents : AppDestination {
        override val route: String = "incidents"
    }

    data object Notifications : AppDestination {
        override val route: String = "notifications"
    }

    data object Appliances : AppDestination {
        override val route: String = "appliances"
    }

    data object Profile : AppDestination {
        override val route: String = "profile"
    }

    data object Settings : AppDestination {
        override val route: String = "settings"
    }

    data object SafetyInformation : AppDestination {
        override val route: String = "safety-information"
    }

    data object Reviews : AppDestination {
        override val route: String = "reviews"
    }
}
