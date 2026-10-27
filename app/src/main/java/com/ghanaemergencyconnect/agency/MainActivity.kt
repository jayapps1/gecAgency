package com.ghanaemergencyconnect.agency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ghanaemergencyconnect.agency.core.ui.theme.GhanaEmergencyConnectAgencyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GhanaEmergencyConnectAgencyTheme {
                GecAgencyApp()
            }
        }
    }
}
