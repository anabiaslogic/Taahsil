package com.example.taahsil.ui.dashboard

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
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Receipt
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
fun DashboardScreen(
    onNavigateToShipments: () -> Unit = {},
    onNavigateToPayments: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProducts: () -> Unit = {},
    onNavigateToCurrency: () -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your trade operations at a glance",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Active Shipments — Large Bento Card
        item {
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToShipments
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Active Shipments",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${state.activeShipments.size}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = ElectricBlue
                        )
                    }
                    Icon(
                        imageVector = Icons.Rounded.LocalShipping,
                        contentDescription = null,
                        tint = ElectricBlue.copy(alpha = 0.3f),
                        modifier = Modifier.height(48.dp).width(48.dp)
                    )
                }

                if (state.activeShipments.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    state.activeShipments.take(2).forEach { shipment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${shipment.originPort} → ${shipment.destinationPort}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Tracking: ${shipment.shipmentTracking}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GrayText
                                )
                            }
                            StatusBadge(
                                text = shipment.shipmentStatus,
                                type = when (shipment.shipmentStatus) {
                                    "Delivered" -> BadgeType.SUCCESS
                                    "In Transit" -> BadgeType.INFO
                                    else -> BadgeType.PENDING
                                }
                            )
                        }
                    }
                }
            }
        }

        // Second row: Pending Payments + Total Products
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BentoCard(modifier = Modifier.weight(1f), onClick = onNavigateToPayments) {
                    MetricContent(
                        icon = Icons.Rounded.Payments,
                        label = "Pending Payments",
                        value = "${state.pendingPayments.size}",
                        color = Emerald
                    )
                }
                BentoCard(modifier = Modifier.weight(1f), onClick = onNavigateToProducts) {
                    MetricContent(
                        icon = Icons.Rounded.Inventory2,
                        label = "Total Products",
                        value = "${state.totalProducts}",
                        color = ElectricBlue
                    )
                }
            }
        }

        // New feature row: Currency + Analytics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BentoCard(modifier = Modifier.weight(1f), onClick = onNavigateToCurrency) {
                    Icon(
                        Icons.Rounded.CurrencyExchange,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B).copy(alpha = 0.35f),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Currency\nConverter", style = MaterialTheme.typography.labelMedium, color = GrayText)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "11 Currencies",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFF59E0B)
                    )
                }
                BentoCard(modifier = Modifier.weight(1f), onClick = onNavigateToAnalytics) {
                    Icon(
                        Icons.Rounded.Analytics,
                        contentDescription = null,
                        tint = Emerald.copy(alpha = 0.35f),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Trade\nAnalytics", style = MaterialTheme.typography.labelMedium, color = GrayText)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "↑ 18.4% YoY",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Emerald
                    )
                }
            }
        }

        // Recent Orders section
        item {
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToOrders
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Orders",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(Icons.Rounded.Receipt, contentDescription = null, tint = GrayText)
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (state.recentOrders.isEmpty()) {
                    Text(
                        text = "No recent orders",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText
                    )
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
                                    text = "#${order.orderId}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "₹${String.format("%,.0f", order.totalAmount)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GrayText
                                )
                            }
                            StatusBadge(
                                text = order.status,
                                type = when (order.status) {
                                    "Completed" -> BadgeType.SUCCESS
                                    "Processing" -> BadgeType.INFO
                                    "Cancelled" -> BadgeType.ERROR
                                    else -> BadgeType.PENDING
                                }
                            )
                        }
                    }
                }
            }
        }

        // Bottom spacing for nav bar
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun MetricContent(
    icon: ImageVector,
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color.copy(alpha = 0.3f),
        modifier = Modifier.height(32.dp).width(32.dp)
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
