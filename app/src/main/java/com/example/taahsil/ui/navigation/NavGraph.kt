package com.example.taahsil.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taahsil.ui.auth.LoginScreen
import com.example.taahsil.ui.auth.SignupScreen
import com.example.taahsil.ui.dashboard.DashboardScreen
import com.example.taahsil.ui.documents.DocumentsScreen
import com.example.taahsil.ui.orders.OrdersScreen
import com.example.taahsil.ui.payments.PaymentsScreen
import com.example.taahsil.ui.products.ProductsScreen
import com.example.taahsil.ui.shipments.ShipmentsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    object Products : Screen("products")
    object Orders : Screen("orders")
    object Shipments : Screen("shipments")
    object Payments : Screen("payments")
    object Documents : Screen("documents")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToShipments = { navController.navigate(Screen.Shipments.route) },
                onNavigateToPayments = { navController.navigate(Screen.Payments.route) },
                onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                onNavigateToProducts = { navController.navigate(Screen.Products.route) }
            )
        }

        composable(Screen.Products.route) {
            ProductsScreen()
        }

        composable(Screen.Orders.route) {
            OrdersScreen()
        }

        composable(Screen.Shipments.route) {
            ShipmentsScreen()
        }

        composable(Screen.Payments.route) {
            PaymentsScreen()
        }

        composable(Screen.Documents.route) {
            DocumentsScreen()
        }
    }
}
