package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.core.navigation.AppDestination
import com.ghanaemergencyconnect.agency.core.navigation.RoleNavigationPolicy
import com.ghanaemergencyconnect.agency.data.domain.model.StaffRole
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResponderWorkspaceTest {

    @Test
    fun responderRole_isReadOnlyPolicy() {
        val role = StaffRole.RESPONDER

        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Dashboard))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Incidents))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Notifications))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Appliances))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Profile))
    }

    @Test
    fun systemAdminRole_isOversightOnlyPolicy() {
        val role = StaffRole.SYSTEM_ADMIN

        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Dashboard))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Notifications))
        assertTrue(RoleNavigationPolicy.isAllowed(role, AppDestination.Profile))
        assertFalse(RoleNavigationPolicy.isAllowed(role, AppDestination.Incidents))
        assertFalse(RoleNavigationPolicy.isAllowed(role, AppDestination.Appliances))
    }
}
