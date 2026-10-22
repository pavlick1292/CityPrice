package com.example.cityprice

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cityprice.data.repository.SettingsRepository
import com.example.cityprice.data.repository.ThemeMode
import com.example.cityprice.ui.navigation.Destination
import com.example.cityprice.ui.screens.cart.CartScreen
import com.example.cityprice.ui.screens.home.HomeScreen
import com.example.cityprice.ui.screens.onboarding.OnboardingScreen
import com.example.cityprice.ui.screens.settings.SettingsScreen
import com.example.cityprice.ui.theme.CityPriceTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val rootViewModel: RootViewModel = hiltViewModel()
            val themeMode by rootViewModel.themeMode.collectAsState()

            val isDark = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            CityPriceTheme(darkTheme = isDark) {
                CityPriceRoot()
            }
        }
    }
}

@HiltViewModel
class RootViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = settingsRepository.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeMode.SYSTEM
        )
}

@Composable
private fun CityPriceRoot(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }
    // Показываем onboarding, если ещё не проходили
    val startDest = remember {
        if (!prefs.getBoolean(KEY_ONBOARDED, false)) Destination.Onboarding.route
        else Destination.Home.route
    }

    // Скрываем bottom bar на onboarding
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute != Destination.Onboarding.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) CityPriceBottomBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDest,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Onboarding.route) {
                OnboardingScreen(onFinish = {
                    prefs.edit().putBoolean(KEY_ONBOARDED, true).apply()
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Onboarding.route) { inclusive = true }
                    }
                })
            }
            composable(Destination.Home.route) { HomeScreen() }
            composable(Destination.Cart.route) { CartScreen() }
            composable(Destination.Settings.route) { SettingsScreen() }
        }
    }
}

@Composable
private fun CityPriceBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        Destination.bottomBarItems.forEach { dest ->
            NavigationBarItem(
                selected = currentRoute == dest.route,
                onClick = {
                    if (currentRoute != dest.route) {
                        navController.navigate(dest.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(dest.icon, contentDescription = dest.label) },
                label = { Text(dest.label) }
            )
        }
    }
}