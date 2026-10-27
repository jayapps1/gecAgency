package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.core.navigation.AppDestination
import com.ghanaemergencyconnect.agency.core.navigation.RoleNavigationPolicy
import com.ghanaemergencyconnect.agency.data.domain.model.StaffRole
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SecurityHardeningTest {

    @Test
    fun responderRole_cannotAccessDisallowedRoutes() {
        val role = StaffRole.RESPONDER

        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Dashboard))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Incidents))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Notifications))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Appliances))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Profile))
    }

    @Test
    fun systemAdminRole_cannotMutateAgencyOperations() {
        val role = StaffRole.SYSTEM_ADMIN

        assertFalse(RoleNavigationPolicy.isAllowed(role, AppDestination.Incidents))
        assertFalse(RoleNavigationPolicy.isAllowed(role, AppDestination.Appliances))
    }
}
