package com.example.taahsil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taahsil.data.DemoDataSeeder
import com.example.taahsil.data.SessionManager
import com.example.taahsil.ui.components.GlassBottomBar
import com.example.taahsil.ui.navigation.NavGraph
import com.example.taahsil.ui.navigation.Screen
import com.example.taahsil.ui.theme.TaahsilTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var demoDataSeeder: DemoDataSeeder
    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaahsilTheme(darkTheme = true) {
                // Seed demo data on first launch
                LaunchedEffect(Unit) {
                    demoDataSeeder.seedIfEmpty()
                }

                val startDest = if (sessionManager.isLoggedIn()) {
                    when (sessionManager.getUserRole()) {
                        "Admin" -> Screen.AdminDashboard.route
                        else -> Screen.Dashboard.route
                    }
                } else {
                    Screen.Login.route
                }

                TaahsilApp(
                    startDestination = startDest,
                    isAdmin = sessionManager.getUserRole() == "Admin",
                    sessionManager = sessionManager
                )
            }
        }
    }
}

@Composable
fun TaahsilApp(
    startDestination: String,
    isAdmin: Boolean,
    sessionManager: com.example.taahsil.data.SessionManager
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    // Routes that should show the bottom nav bar
    val mainRoutes = listOf(
        Screen.Dashboard.route,
        Screen.AdminDashboard.route,
        Screen.UserManagement.route,
        Screen.Products.route,
        Screen.Orders.route,
        Screen.Shipments.route,
        Screen.Payments.route,
        Screen.Documents.route,
        Screen.Currency.route,
        Screen.Analytics.route,
        Screen.Profile.route
    )
    val showBottomBar = currentRoute in mainRoutes

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavGraph(
                navController = navController,
                startDestination = startDestination,
                sessionManager = sessionManager
            )

            if (showBottomBar) {
                GlassBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        val popUpRoute = if (isAdmin) Screen.AdminDashboard.route else Screen.Dashboard.route
                        navController.navigate(route) {
                            popUpTo(popUpRoute) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    isAdmin = isAdmin,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}