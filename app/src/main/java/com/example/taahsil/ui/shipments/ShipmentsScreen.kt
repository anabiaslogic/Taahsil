package com.example.taahsil.ui.shipments

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taahsil.data.local.entity.ShipmentEntity
import com.example.taahsil.ui.components.BadgeType
import com.example.taahsil.ui.components.BentoCard
import com.example.taahsil.ui.components.StatusBadge
import com.example.taahsil.ui.components.TrackingStep
import com.example.taahsil.ui.components.TrackingTimeline
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.GrayText

@Composable
fun ShipmentsScreen(
    viewModel: ShipmentViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Shipments",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Track your logistics in real-time",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (state.shipments.isEmpty()) {
            item {
                BentoCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Rounded.LocalShipping,
                            contentDescription = null,
                            tint = GrayText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No active shipments", color = GrayText, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        items(state.shipments) { shipment ->
            ShipmentCard(shipment = shipment)
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun ShipmentCard(shipment: ShipmentEntity) {
    var isExpanded by remember { mutableStateOf(false) }

    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { isExpanded = !isExpanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.LocalShipping,
                        contentDescription = null,
                        tint = ElectricBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = shipment.shipmentTracking,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${shipment.originPort} → ${shipment.destinationPort}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayText
                )
            }
            StatusBadge(
                text = shipment.shipmentStatus,
                type = when (shipment.shipmentStatus) {
                    "Delivered" -> BadgeType.SUCCESS
                    "In Transit" -> BadgeType.INFO
                    "Customs" -> BadgeType.PENDING
                    else -> BadgeType.PENDING
                }
            )
        }

        // Expandable tracking timeline
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Bill Type: ${shipment.billType}",
                    style = MaterialTheme.typography.labelMedium,
                    color = GrayText
                )
                Spacer(modifier = Modifier.height(8.dp))

                TrackingTimeline(
                    steps = getTrackingSteps(shipment)
                )
            }
        }
    }
}

private fun getTrackingSteps(shipment: ShipmentEntity): List<TrackingStep> {
    val statusIndex = when (shipment.shipmentStatus) {
        "Booked" -> 0
        "In Transit" -> 1
        "Customs" -> 2
        "Delivered" -> 3
        else -> 0
    }

    return listOf(
        TrackingStep(
            title = "Order Booked",
            subtitle = "Origin: ${shipment.originPort}",
            isCompleted = statusIndex > 0,
            isCurrent = statusIndex == 0
        ),
        TrackingStep(
            title = "In Transit",
            subtitle = "Shipment on the way",
            isCompleted = statusIndex > 1,
            isCurrent = statusIndex == 1
        ),
        TrackingStep(
            title = "Customs Clearance",
            subtitle = "Processing at destination",
            isCompleted = statusIndex > 2,
            isCurrent = statusIndex == 2
        ),
        TrackingStep(
            title = "Delivered",
            subtitle = "Destination: ${shipment.destinationPort}",
            isCompleted = statusIndex >= 3,
            isCurrent = statusIndex == 3
        )
    )
}
