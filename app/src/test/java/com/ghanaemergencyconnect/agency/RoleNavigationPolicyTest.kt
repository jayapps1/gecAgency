package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.core.navigation.AppDestination
import com.ghanaemergencyconnect.agency.core.navigation.RoleNavigationPolicy
import com.ghanaemergencyconnect.agency.data.domain.model.StaffRole
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RoleNavigationPolicyTest {

    @Test
    fun responder_isAllowedOperationalDestinations() {
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.RESPONDER, AppDestination.Dashboard))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.RESPONDER, AppDestination.Incidents))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.RESPONDER, AppDestination.Notifications))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.RESPONDER, AppDestination.Appliances))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.RESPONDER, AppDestination.Profile))
    }

    @Test
    fun agencyAdmin_isAllowedOperationalDestinations() {
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.AGENCY_ADMIN, AppDestination.Dashboard))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.AGENCY_ADMIN, AppDestination.Incidents))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.AGENCY_ADMIN, AppDestination.Notifications))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.AGENCY_ADMIN, AppDestination.Appliances))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.AGENCY_ADMIN, AppDestination.Profile))
    }

    @Test
    fun systemAdmin_hasOversightAccess() {
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.SYSTEM_ADMIN, AppDestination.Dashboard))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.SYSTEM_ADMIN, AppDestination.Notifications))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.SYSTEM_ADMIN, AppDestination.Profile))
        assertFalse(RoleNavigationPolicy.isAllowed(StaffRole.SYSTEM_ADMIN, AppDestination.Incidents))
        assertFalse(RoleNavigationPolicy.isAllowed(StaffRole.SYSTEM_ADMIN, AppDestination.Appliances))
    }

    @Test
    fun unknownRole_isRestrictedToSafeDestinations() {
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.UNKNOWN, AppDestination.Dashboard))
        assertTrue(RoleNavigationPolicy.isAllowed(StaffRole.UNKNOWN, AppDestination.Profile))
        assertFalse(RoleNavigationPolicy.isAllowed(StaffRole.UNKNOWN, AppDestination.Incidents))
        assertFalse(RoleNavigationPolicy.isAllowed(StaffRole.UNKNOWN, AppDestination.Appliances))
    }

    @Test
    fun bottomNavigation_containsCorrectTopLevelItems() {
        val responderItems = RoleNavigationPolicy.getBottomNavItems(StaffRole.RESPONDER)
        assertTrue(responderItems.contains(AppDestination.Dashboard))
        assertTrue(responderItems.contains(AppDestination.Incidents))
        assertTrue(responderItems.contains(AppDestination.Notifications))
        assertTrue(responderItems.contains(AppDestination.Profile))

        val unknownItems = RoleNavigationPolicy.getBottomNavItems(StaffRole.UNKNOWN)
        assertTrue(unknownItems.contains(AppDestination.Dashboard))
        assertTrue(unknownItems.contains(AppDestination.Profile))
        assertFalse(unknownItems.contains(AppDestination.Incidents))
    }
}
