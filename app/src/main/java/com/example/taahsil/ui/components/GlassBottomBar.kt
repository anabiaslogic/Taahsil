package com.example.taahsil.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.taahsil.ui.theme.ElectricBlue

data class NavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

val bottomNavItems = listOf(
    NavItem(Icons.Rounded.Dashboard, "Home", "dashboard"),
    NavItem(Icons.Rounded.Inventory2, "Products", "products"),
    NavItem(Icons.Rounded.Receipt, "Orders", "orders"),
    NavItem(Icons.Rounded.LocalShipping, "Shipments", "shipments"),
    NavItem(Icons.Rounded.Payments, "Payments", "payments")
)

val adminBottomNavItems = listOf(
    NavItem(Icons.Rounded.AdminPanelSettings, "Admin", "admin_dashboard"),
    NavItem(Icons.Rounded.Group, "Users", "user_management"),
    NavItem(Icons.Rounded.Inventory2, "Products", "products"),
    NavItem(Icons.Rounded.Receipt, "Orders", "orders"),
    NavItem(Icons.Rounded.LocalShipping, "Shipments", "shipments")
)

@Composable
fun GlassBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    isAdmin: Boolean = false,
    modifier: Modifier = Modifier
) {
    val items = if (isAdmin) adminBottomNavItems else bottomNavItems

    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
            )
            .height(64.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            IconButton(
                onClick = { onNavigate(item.route) }
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isSelected) ElectricBlue
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(if (isSelected) 28.dp else 24.dp)
                )
            }
        }
    }
}
