package com.ghanaemergencyconnect.agency.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.ghanaemergencyconnect.agency.core.auth.SessionState
import com.ghanaemergencyconnect.agency.core.ui.component.AccessDeniedScreen
import com.ghanaemergencyconnect.agency.core.ui.component.GecBottomNavigation
import com.ghanaemergencyconnect.agency.data.domain.repository.ApplianceRepository
import com.ghanaemergencyconnect.agency.data.domain.repository.AuthRepository
import com.ghanaemergencyconnect.agency.data.domain.repository.IncidentRepository
import com.ghanaemergencyconnect.agency.data.domain.repository.NotificationRepository
import com.ghanaemergencyconnect.agency.data.domain.repository.ReviewRepository
import com.ghanaemergencyconnect.agency.data.domain.repository.SafetyRepository
import com.ghanaemergencyconnect.agency.feature.appliances.AppliancesScreen
import com.ghanaemergencyconnect.agency.feature.appliances.AppliancesViewModel
import com.ghanaemergencyconnect.agency.feature.auth.ChangePasswordMode
import com.ghanaemergencyconnect.agency.feature.auth.ChangePasswordScreen
import com.ghanaemergencyconnect.agency.feature.auth.ChangePasswordViewModel
import com.ghanaemergencyconnect.agency.feature.auth.LoginScreen
import com.ghanaemergencyconnect.agency.feature.auth.LoginViewModel
import com.ghanaemergencyconnect.agency.feature.dashboard.DashboardScreen
import com.ghanaemergencyconnect.agency.feature.incidentdetail.IncidentDetailScreen
import com.ghanaemergencyconnect.agency.feature.incidentdetail.IncidentDetailViewModel
import com.ghanaemergencyconnect.agency.feature.incidents.IncidentsScreen
import com.ghanaemergencyconnect.agency.feature.incidents.IncidentsViewModel
import com.ghanaemergencyconnect.agency.feature.notifications.NotificationsScreen
import com.ghanaemergencyconnect.agency.feature.notifications.NotificationsViewModel
import com.ghanaemergencyconnect.agency.feature.profile.ProfileScreen
import com.ghanaemergencyconnect.agency.feature.reviews.ReviewListScreen
import com.ghanaemergencyconnect.agency.feature.reviews.ReviewsViewModel
import com.ghanaemergencyconnect.agency.feature.safety.SafetyInformationScreen
import com.ghanaemergencyconnect.agency.feature.safety.SafetyInformationViewModel
import com.ghanaemergencyconnect.agency.feature.settings.SettingsScreen
import com.ghanaemergencyconnect.agency.feature.splash.SplashScreen
import com.ghanaemergencyconnect.agency.feature.splash.SplashViewModel
import kotlinx.coroutines.launch

inline fun <reified VM : ViewModel> viewModelFactory(crossinline initializer: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return initializer() as T
        }
    }
}

/**
 * Central navigation graph for the staff operations application.
 */
@Composable
fun GecNavHost(
    navController: NavHostController,
    authRepository: com.ghanaemergencyconnect.agency.data.domain.repository.AuthRepository,
    incidentRepository: com.ghanaemergencyconnect.agency.data.domain.repository.IncidentRepository,
    notificationRepository: com.ghanaemergencyconnect.agency.data.domain.repository.NotificationRepository,
    applianceRepository: com.ghanaemergencyconnect.agency.data.domain.repository.ApplianceRepository,
    safetyRepository: com.ghanaemergencyconnect.agency.data.domain.repository.SafetyRepository,
    reviewRepository: com.ghanaemergencyconnect.agency.data.domain.repository.ReviewRepository
) {
    val sessionState by authRepository.sessionState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val staff = (sessionState as? SessionState.Authenticated)?.staff
    val role = staff?.primaryRole

    val bottomNavItems = if (role != null) {
        RoleNavigationPolicy.getBottomNavItems(role)
    } else {
        emptyList()
    }

    val isTopLevelRoute = currentRoute in listOf(
        AppDestination.Dashboard.route,
        AppDestination.Incidents.route,
        AppDestination.Notifications.route,
        AppDestination.Profile.route,
        AppDestination.Settings.route
    )

    fun navigateTopLevel(destination: AppDestination) {
        if (currentRoute != destination.route) {
            navController.navigate(destination.route) {
                popUpTo(AppDestination.Dashboard.route) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (isTopLevelRoute && bottomNavItems.isNotEmpty()) {
                GecBottomNavigation(
                    currentDestination = currentRoute,
                    destinations = bottomNavItems,
                    onDestinationSelected = { destination ->
                        navigateTopLevel(destination)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.Splash.route) { backStackEntry ->
                val splashViewModel: SplashViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelFactory { SplashViewModel(authRepository) }
                )
                SplashScreen(
                    viewModel = splashViewModel,
                    onNavigateToDashboard = {
                        if (staff?.mustChangePassword == true) {
                            navController.navigate(AppDestination.ChangePassword.createRoute(mandatory = true)) {
                                popUpTo(AppDestination.Splash.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(AppDestination.Dashboard.route) {
                                popUpTo(AppDestination.Splash.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(AppDestination.Login.route) {
                            popUpTo(AppDestination.Splash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AppDestination.Login.route) { backStackEntry ->
                val loginViewModel: LoginViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelFactory { LoginViewModel(authRepository) }
                )
                val loginUiState by loginViewModel.uiState.collectAsStateWithLifecycle()

                LoginScreen(
                    uiState = loginUiState,
                    onModeSelected = { loginViewModel.selectMode(it) },
                    onIdentifierChanged = { loginViewModel.onIdentifierChanged(it) },
                    onPasswordChanged = { loginViewModel.onPasswordChanged(it) },
                    onCodeChanged = { loginViewModel.onAuthenticatorCodeChanged(it) },
                    onSubmitPassword = { loginViewModel.submitPasswordLogin() },
                    onSubmitAuthenticator = { loginViewModel.submitAuthenticatorLogin() },
                    onLoginSuccess = {
                        if (staff?.mustChangePassword == true) {
                            navController.navigate(AppDestination.ChangePassword.createRoute(mandatory = true)) {
                                popUpTo(AppDestination.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(AppDestination.Dashboard.route) {
                                popUpTo(AppDestination.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable(AppDestination.Dashboard.route) {
                DashboardScreen(
                    staff = staff,
                    onNavigateToIncidents = { navigateTopLevel(AppDestination.Incidents) },
                    onNavigateToNotifications = { navigateTopLevel(AppDestination.Notifications) },
                    onNavigateToAppliances = { navController.navigate(AppDestination.Appliances.route) },
                    onNavigateToProfile = { navigateTopLevel(AppDestination.Profile) }
                )
            }

            composable(AppDestination.Incidents.route) { backStackEntry ->
                if (role != null && RoleNavigationPolicy.isAllowed(role, AppDestination.Incidents)) {
                    val incidentsViewModel: IncidentsViewModel = viewModel(
                        viewModelStoreOwner = backStackEntry,
                        factory = viewModelFactory { IncidentsViewModel(incidentRepository) }
                    )
                    val incidentsUiState by incidentsViewModel.uiState.collectAsStateWithLifecycle()

                    IncidentsScreen(
                        role = role,
                        uiState = incidentsUiState,
                        onFilterSelected = { incidentsViewModel.selectFilter(it) },
                        onRefresh = { incidentsViewModel.loadIncidents() },
                        onIncidentClick = { reportId ->
                            navController.navigate("incidents/$reportId")
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                } else {
                    AccessDeniedScreen(onBackClick = { navController.popBackStack() })
                }
            }

            composable(
                route = "incidents/{reportId}",
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
                val detailViewModel: IncidentDetailViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelFactory { IncidentDetailViewModel(incidentRepository) }
                )
                val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()

                IncidentDetailScreen(
                    reportId = reportId,
                    staff = staff,
                    uiState = detailUiState,
                    onRefresh = { detailViewModel.loadDetail(reportId) },
                    onAcknowledge = { detailViewModel.acknowledgeReport(reportId) },
                    onStartAction = { detailViewModel.startAction(reportId) },
                    onResolve = { note -> detailViewModel.resolveReport(reportId, note) },
                    onLoadEligibleResponders = { detailViewModel.loadEligibleResponders(reportId) },
                    onAssignResponder = { responderUserId -> detailViewModel.assignResponder(reportId, responderUserId) },
                    onReleaseResponder = { assignmentId -> detailViewModel.releaseResponder(reportId, assignmentId) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(AppDestination.Notifications.route) { backStackEntry ->
                if (role != null && RoleNavigationPolicy.isAllowed(role, AppDestination.Notifications)) {
                    val notificationsViewModel: NotificationsViewModel = viewModel(
                        viewModelStoreOwner = backStackEntry,
                        factory = viewModelFactory { NotificationsViewModel(notificationRepository) }
                    )
                    val notificationsUiState by notificationsViewModel.uiState.collectAsStateWithLifecycle()

                    NotificationsScreen(
                        uiState = notificationsUiState,
                        onRefresh = { notificationsViewModel.loadNotifications() },
                        onNotificationClick = { notification ->
                            notificationsViewModel.markAsRead(notification.id)
                            if (!notification.reportId.isNullOrBlank()) {
                                navController.navigate("incidents/${notification.reportId}")
                            }
                        },
                        onMarkAllRead = { notificationsViewModel.markAllAsRead() },
                        onBackClick = { navController.popBackStack() }
                    )
                } else {
                    AccessDeniedScreen(onBackClick = { navController.popBackStack() })
                }
            }

            composable(AppDestination.Appliances.route) { backStackEntry ->
                if (role != null && RoleNavigationPolicy.isAllowed(role, AppDestination.Appliances)) {
                    val appliancesViewModel: AppliancesViewModel = viewModel(
                        viewModelStoreOwner = backStackEntry,
                        factory = viewModelFactory { AppliancesViewModel(applianceRepository) }
                    )
                    val appliancesUiState by appliancesViewModel.uiState.collectAsStateWithLifecycle()

                    AppliancesScreen(
                        role = role,
                        uiState = appliancesUiState,
                        onFilterSelected = { appliancesViewModel.selectFilter(it) },
                        onSearchQueryChanged = { appliancesViewModel.updateSearchQuery(it) },
                        onRefresh = { appliancesViewModel.loadAppliances() },
                        onBackClick = { navController.popBackStack() }
                    )
                } else {
                    AccessDeniedScreen(onBackClick = { navController.popBackStack() })
                }
            }

            composable(AppDestination.SafetyInformation.route) { backStackEntry ->
                val safetyViewModel: SafetyInformationViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelFactory { SafetyInformationViewModel(safetyRepository) }
                )
                val safetyUiState by safetyViewModel.uiState.collectAsStateWithLifecycle()

                SafetyInformationScreen(
                    uiState = safetyUiState,
                    onRefresh = { safetyViewModel.loadPosts() },
                    onCreatePost = { title, content, category, severity, notifyUsers ->
                        safetyViewModel.createAndPublishPost(title, content, category, severity, notifyUsers)
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(AppDestination.Reviews.route) { backStackEntry ->
                val reviewsViewModel: ReviewsViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelFactory { ReviewsViewModel(reviewRepository) }
                )
                val reviewsUiState by reviewsViewModel.uiState.collectAsStateWithLifecycle()

                ReviewListScreen(
                    uiState = reviewsUiState,
                    onRefresh = { reviewsViewModel.loadReviews() },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(AppDestination.Profile.route) {
                ProfileScreen(
                    staff = staff,
                    onChangePasswordClick = {
                        navController.navigate(AppDestination.ChangePassword.createRoute(mandatory = false))
                    },
                    onSignOut = {
                        scope.launch {
                            authRepository.logout()
                            navController.navigate(AppDestination.Login.route) {
                                popUpTo(AppDestination.Dashboard.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(AppDestination.Settings.route) {
                SettingsScreen(
                    onSignOut = {
                        scope.launch {
                            authRepository.logout()
                            navController.navigate(AppDestination.Login.route) {
                                popUpTo(AppDestination.Dashboard.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = AppDestination.ChangePassword.route,
                arguments = listOf(navArgument("mandatory") {
                    type = NavType.BoolType
                    defaultValue = false
                })
            ) { backStackEntry ->
                val mandatory = backStackEntry.arguments?.getBoolean("mandatory") ?: false
                val changePasswordViewModel: ChangePasswordViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelFactory { ChangePasswordViewModel(authRepository) }
                )
                val changePasswordUiState by changePasswordViewModel.uiState.collectAsStateWithLifecycle()

                ChangePasswordScreen(
                    mode = if (mandatory) ChangePasswordMode.Mandatory else ChangePasswordMode.Voluntary,
                    uiState = changePasswordUiState,
                    onCurrentPasswordChanged = { changePasswordViewModel.onCurrentPasswordChanged(it) },
                    onNewPasswordChanged = { changePasswordViewModel.onNewPasswordChanged(it) },
                    onConfirmPasswordChanged = { changePasswordViewModel.onConfirmPasswordChanged(it) },
                    onToggleCurrentVisibility = { changePasswordViewModel.toggleCurrentVisibility() },
                    onToggleNewVisibility = { changePasswordViewModel.toggleNewVisibility() },
                    onToggleConfirmVisibility = { changePasswordViewModel.toggleConfirmVisibility() },
                    onSubmit = { changePasswordViewModel.submitChangePassword() },
                    onSuccessComplete = {
                        if (mandatory) {
                            navController.navigate(AppDestination.Dashboard.route) {
                                popUpTo(AppDestination.ChangePassword.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            navController.popBackStack()
                        }
                    },
                    onSignOut = {
                        scope.launch {
                            authRepository.logout()
                            navController.navigate(AppDestination.Login.route) {
                                popUpTo(AppDestination.Dashboard.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
