package com.example.cityprice.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Destination("home", "Поиск", Icons.Filled.Search)
    data object Cart : Destination("cart", "Корзина", Icons.Filled.ShoppingCart)
    data object Settings : Destination("settings", "Настройки", Icons.Filled.Settings)

    /** Онбординг — не вкладка, но нужен как стартовый экран при первом запуске. */
    data object Onboarding : Destination("onboarding", "Онбординг", Icons.Filled.Search)

    companion object {
        val bottomBarItems = listOf(Home, Cart, Settings)
    }
}