package com.example.taahsil.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taahsil.ui.components.BadgeType
import com.example.taahsil.ui.components.BentoCard
import com.example.taahsil.ui.components.StatusBadge
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.Emerald
import com.example.taahsil.ui.theme.GrayText

@Composable
fun AdminDashboardScreen(
    onNavigateToUsers: () -> Unit = {},
    onNavigateToProducts: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToShipments: () -> Unit = {},
    onNavigateToPayments: () -> Unit = {},
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.AdminPanelSettings,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Admin Panel",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Business analytics & management",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText
                    )
                }
            }
        }

        // Revenue card — large
        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Revenue", style = MaterialTheme.typography.labelMedium, color = GrayText)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "₹${String.format("%,.2f", state.totalRevenue)}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Emerald
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.TrendingUp,
                                contentDescription = null,
                                tint = Emerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Across ${state.totalOrders} orders",
                                style = MaterialTheme.typography.labelMedium,
                                color = Emerald
                            )
                        }
                    }
                    Icon(
                        Icons.Rounded.AttachMoney,
                        contentDescription = null,
                        tint = Emerald.copy(alpha = 0.2f),
                        modifier = Modifier.size(56.dp)
                    )
                }
            }
        }

        // Stats row 1: Users + Products
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BentoCard(modifier = Modifier.weight(1f), onClick = onNavigateToUsers) {
                    AdminMetric(
                        icon = Icons.Rounded.Group,
                        label = "Users",
                        value = "${state.users.size}",
                        color = ElectricBlue
                    )
                }
                BentoCard(modifier = Modifier.weight(1f), onClick = onNavigateToProducts) {
                    AdminMetric(
                        icon = Icons.Rounded.Inventory2,
                        label = "Products",
                        value = "${state.totalProducts}",
                        color = Color(0xFFF59E0B)
                    )
                }
            }
        }

        // Stats row 2: Shipments + Payments
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BentoCard(modifier = Modifier.weight(1f), onClick = onNavigateToShipments) {
                    AdminMetric(
                        icon = Icons.Rounded.LocalShipping,
                        label = "Active Shipments",
                        value = "${state.activeShipments}",
                        color = ElectricBlue
                    )
                }
                BentoCard(modifier = Modifier.weight(1f), onClick = onNavigateToPayments) {
                    AdminMetric(
                        icon = Icons.Rounded.Payments,
                        label = "Pending Payments",
                        value = "${state.pendingPayments}",
                        color = Color(0xFFF59E0B)
                    )
                }
            }
        }

        // Payment breakdown
        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Payment Overview",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Collected", style = MaterialTheme.typography.labelMedium, color = GrayText)
                        Text(
                            "₹${String.format("%,.2f", state.paidAmount)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = Emerald
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Outstanding", style = MaterialTheme.typography.labelMedium, color = GrayText)
                        Text(
                            "₹${String.format("%,.2f", state.pendingAmount)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFF59E0B)
                        )
                    }
                }
            }
        }

        // Recent Orders overview
        item {
            BentoCard(modifier = Modifier.fillMaxWidth(), onClick = onNavigateToOrders) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Orders",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp)
                    )
                    Icon(Icons.Rounded.Receipt, contentDescription = null, tint = GrayText)
                }
                Spacer(modifier = Modifier.height(12.dp))
                if (state.recentOrders.isEmpty()) {
                    Text("No orders yet", color = GrayText, style = MaterialTheme.typography.bodyMedium)
                } else {
                    state.recentOrders.forEach { order ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "#${order.orderId}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    "₹${String.format("%,.2f", order.totalAmount)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GrayText
                                )
                            }
                            StatusBadge(
                                text = order.status,
                                type = when (order.status) {
                                    "Completed" -> BadgeType.SUCCESS
                                    "Processing" -> BadgeType.INFO
                                    else -> BadgeType.PENDING
                                }
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun AdminMetric(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color.copy(alpha = 0.3f),
        modifier = Modifier.size(28.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = label, style = MaterialTheme.typography.labelMedium, color = GrayText)
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = value,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = color
    )
}
