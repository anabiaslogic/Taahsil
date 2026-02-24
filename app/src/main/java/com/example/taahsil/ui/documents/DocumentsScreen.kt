package com.example.taahsil.ui.documents

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
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taahsil.ui.components.BentoCard
import com.example.taahsil.ui.components.BadgeType
import com.example.taahsil.ui.components.StatusBadge
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.GrayText

data class DocumentItem(
    val title: String,
    val type: String,
    val shipmentId: String,
    val date: String
)

@Composable
fun DocumentsScreen() {
    // Sample documents - in production these would come from a ViewModel
    val documents = listOf(
        DocumentItem("Bill of Lading", "BOL", "SHP-001", "2024-03-15"),
        DocumentItem("Customs Declaration", "CUSTOMS", "SHP-001", "2024-03-16"),
        DocumentItem("Commercial Invoice", "INVOICE", "SHP-002", "2024-03-10"),
        DocumentItem("Packing List", "PACKING", "SHP-002", "2024-03-10"),
        DocumentItem("Certificate of Origin", "COO", "SHP-003", "2024-03-08")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Documents",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Trade documents & customs paperwork",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.FolderOpen,
                        contentDescription = null,
                        tint = ElectricBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${documents.size} Documents",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "Available for offline viewing",
                            style = MaterialTheme.typography.labelMedium,
                            color = GrayText
                        )
                    }
                }
            }
        }

        documents.forEach { doc ->
            item {
                BentoCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                if (doc.type == "BOL" || doc.type == "INVOICE")
                                    Icons.Rounded.PictureAsPdf
                                else Icons.Rounded.Description,
                                contentDescription = null,
                                tint = ElectricBlue.copy(alpha = 0.6f),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = doc.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "Shipment: ${doc.shipmentId} · ${doc.date}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GrayText
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusBadge(
                                text = doc.type,
                                type = BadgeType.INFO
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { /* Download document */ }) {
                                Icon(
                                    Icons.Rounded.Download,
                                    contentDescription = "Download",
                                    tint = ElectricBlue
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
