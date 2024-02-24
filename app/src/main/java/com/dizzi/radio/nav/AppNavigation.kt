package com.dizzi.radio.nav

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dizzi.radio.screens.AboutScreen
import com.dizzi.radio.screens.AppViewModel
import com.dizzi.radio.screens.PrivacyPolicyScreen
import com.dizzi.radio.screens.RadioPlayerScreen
import com.dizzi.radio.screens.RequestASongScreen
import com.dizzi.radio.screens.TermsAndConditionScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    drawerState: DrawerState,
    appViewModel: AppViewModel
) {
    NavHost(navController = navController, startDestination = Screens.RadioPlayerScreen.route) {
        Screens.values().forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    Screens.RadioPlayerScreen -> RadioPlayerScreen(drawerState, appViewModel)
                    Screens.AboutScreen -> AboutScreen(drawerState)
                    Screens.TermsAndConditionsScreen -> TermsAndConditionScreen(drawerState)
                    Screens.PrivacyPolicyScreen -> PrivacyPolicyScreen(drawerState)
                    Screens.RequestASongScreen -> RequestASongScreen(drawerState)
                    else -> {

                    }
                }
            }
        }
    }
}

