package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.anc.AncScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.device.DeviceDetailsScreen
import com.example.ui.equalizer.EqualizerScreen
import com.example.ui.finder.FinderScreen
import com.example.ui.gestures.GesturesScreen
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.popup.AirPodsPopup
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.KaranBudsTheme
import com.example.viewmodel.BudsViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BudsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.appSettings.collectAsState()
            val showPopup by viewModel.showPopup.collectAsState()
            val popupDevice by viewModel.popupDevice.collectAsState()

            var isOnboarded by remember { mutableStateOf(false) }

            KaranBudsTheme(
                themeMode = settings.themeMode,
                accentColorIndex = settings.accentColorIndex
            ) {
                val navController = rememberNavController()

                Box(modifier = Modifier.fillMaxSize()) {
                    if (!isOnboarded) {
                        OnboardingScreen(onFinish = { isOnboarded = true })
                    } else {
                        NavHost(navController = navController, startDestination = "dashboard") {
                            composable("dashboard") {
                                DashboardScreen(viewModel = viewModel) { route ->
                                    navController.navigate(route)
                                }
                            }
                            composable("anc") {
                                AncScreen(viewModel = viewModel) {
                                    navController.popBackStack()
                                }
                            }
                            composable("equalizer") {
                                EqualizerScreen(viewModel = viewModel) {
                                    navController.popBackStack()
                                }
                            }
                            composable("gestures") {
                                GesturesScreen(viewModel = viewModel) {
                                    navController.popBackStack()
                                }
                            }
                            composable("finder") {
                                FinderScreen(viewModel = viewModel) {
                                    navController.popBackStack()
                                }
                            }
                            composable("device") {
                                DeviceDetailsScreen(viewModel = viewModel) {
                                    navController.popBackStack()
                                }
                            }
                            composable("settings") {
                                SettingsScreen(viewModel = viewModel) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    }

                    // Apple-style popup overlay
                    AirPodsPopup(
                        device = popupDevice,
                        isVisible = showPopup,
                        onDismiss = { viewModel.dismissPopup() }
                    )
                }
            }
        }
    }
}
