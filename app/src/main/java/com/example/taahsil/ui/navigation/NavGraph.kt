package com.example.taahsil.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taahsil.ui.admin.AdminDashboardScreen
import com.example.taahsil.ui.admin.UserManagementScreen
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
    object AdminDashboard : Screen("admin_dashboard")
    object UserManagement : Screen("user_management")
    object Products : Screen("products")
    object Orders : Screen("orders")
    object Shipments : Screen("shipments")
    object Payments : Screen("payments")
    object Documents : Screen("documents")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        "Admin" -> Screen.AdminDashboard.route
                        else -> Screen.Dashboard.route
                    }
                    navController.navigate(destination) {
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
                onSignupSuccess = { role ->
                    val destination = when (role) {
                        "Admin" -> Screen.AdminDashboard.route
                        else -> Screen.Dashboard.route
                    }
                    navController.navigate(destination) {
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

        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                onNavigateToUsers = { navController.navigate(Screen.UserManagement.route) },
                onNavigateToProducts = { navController.navigate(Screen.Products.route) },
                onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                onNavigateToShipments = { navController.navigate(Screen.Shipments.route) },
                onNavigateToPayments = { navController.navigate(Screen.Payments.route) }
            )
        }

        composable(Screen.UserManagement.route) {
            UserManagementScreen()
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
