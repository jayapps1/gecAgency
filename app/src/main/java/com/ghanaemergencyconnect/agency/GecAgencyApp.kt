package com.ghanaemergencyconnect.agency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.ghanaemergencyconnect.agency.core.navigation.GecNavHost
import com.ghanaemergencyconnect.agency.core.network.RetrofitClient
import com.ghanaemergencyconnect.agency.core.security.SecureSessionManager
import com.ghanaemergencyconnect.agency.data.api.ApplianceApi
import com.ghanaemergencyconnect.agency.data.api.AuthApi
import com.ghanaemergencyconnect.agency.data.api.IncidentApi
import com.ghanaemergencyconnect.agency.data.api.NotificationApi
import com.ghanaemergencyconnect.agency.data.api.ReviewApi
import com.ghanaemergencyconnect.agency.data.api.SafetyApi
import com.ghanaemergencyconnect.agency.data.repository.ApplianceRepositoryImpl
import com.ghanaemergencyconnect.agency.data.repository.AuthRepositoryImpl
import com.ghanaemergencyconnect.agency.data.repository.IncidentRepositoryImpl
import com.ghanaemergencyconnect.agency.data.repository.NotificationRepositoryImpl
import com.ghanaemergencyconnect.agency.data.repository.ReviewRepositoryImpl
import com.ghanaemergencyconnect.agency.data.repository.SafetyRepositoryImpl

/**
 * Root Compose entry point for the staff operations application.
 * The root NavController and core singletons are initialized here.
 */
@Composable
fun GecAgencyApp() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val sessionManager = remember { SecureSessionManager(context) }
    val retrofitClient = remember { RetrofitClient(sessionManager) }
    val authApi = remember { retrofitClient.retrofit.create(AuthApi::class.java) }
    val incidentApi = remember { retrofitClient.retrofit.create(IncidentApi::class.java) }
    val notificationApi = remember { retrofitClient.retrofit.create(NotificationApi::class.java) }
    val applianceApi = remember { retrofitClient.retrofit.create(ApplianceApi::class.java) }
    val safetyApi = remember { retrofitClient.retrofit.create(SafetyApi::class.java) }
    val reviewApi = remember { retrofitClient.retrofit.create(ReviewApi::class.java) }

    val authRepository = remember { AuthRepositoryImpl(authApi, sessionManager) }
    val incidentRepository = remember { IncidentRepositoryImpl(incidentApi) }
    val notificationRepository = remember { NotificationRepositoryImpl(notificationApi, sessionManager) }
    val applianceRepository = remember { ApplianceRepositoryImpl(applianceApi) }
    val safetyRepository = remember { SafetyRepositoryImpl(safetyApi) }
    val reviewRepository = remember { ReviewRepositoryImpl(reviewApi) }

    GecNavHost(
        navController = navController,
        authRepository = authRepository,
        incidentRepository = incidentRepository,
        notificationRepository = notificationRepository,
        applianceRepository = applianceRepository,
        safetyRepository = safetyRepository,
        reviewRepository = reviewRepository
    )
}
