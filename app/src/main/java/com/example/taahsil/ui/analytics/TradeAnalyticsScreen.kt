package com.example.taahsil.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taahsil.ui.components.BadgeType
import com.example.taahsil.ui.components.BentoCard
import com.example.taahsil.ui.components.StatusBadge
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.Emerald
import com.example.taahsil.ui.theme.GrayText

data class MonthlyRevenue(val month: String, val amount: Double)
data class TopProduct(val name: String, val category: String, val revenue: Double, val share: Float)
data class DestinationMarket(val country: String, val flag: String, val shipments: Int, val revenue: Double)

val monthlyData = listOf(
    MonthlyRevenue("Aug", 820000.0),
    MonthlyRevenue("Sep", 1150000.0),
    MonthlyRevenue("Oct", 970000.0),
    MonthlyRevenue("Nov", 1340000.0),
    MonthlyRevenue("Dec", 1890000.0),
    MonthlyRevenue("Jan", 2300000.0),
    MonthlyRevenue("Feb", 2107000.0)
)

val topProducts = listOf(
    TopProduct("Organic Basmati Rice", "Food Grains", 870000.0, 0.28f),
    TopProduct("Hand-Woven Silk Saree", "Textiles", 672000.0, 0.22f),
    TopProduct("Darjeeling Green Tea", "Beverages", 510000.0, 0.17f),
    TopProduct("Cashew Nuts (W-240)", "Dry Fruits", 425000.0, 0.14f),
    TopProduct("Granite Tiles", "Building Material", 315000.0, 0.10f)
)

val destinationMarkets = listOf(
    DestinationMarket("UAE (Dubai)", "\uD83C\uDDE6\uD83C\uDDEA", 12, 1850000.0),
    DestinationMarket("Germany", "\uD83C\uDDE9\uD83C\uDDEA", 8, 1340000.0),
    DestinationMarket("United Kingdom", "\uD83C\uDDEC\uD83C\uDDE7", 6, 1020000.0),
    DestinationMarket("Singapore", "\uD83C\uDDF8\uD83C\uDDEC", 5, 870000.0),
    DestinationMarket("Japan", "\uD83C\uDDEF\uD83C\uDDF5", 4, 680000.0),
    DestinationMarket("Kenya", "\uD83C\uDDF0\uD83C\uDDEA", 3, 315000.0)
)

@Composable
fun TradeAnalyticsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Analytics, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Trade Analytics", style = MaterialTheme.typography.headlineLarge.copy(fontSize = 26.sp),
                        color = MaterialTheme.colorScheme.onBackground)
                    Text("Export performance & market insights", style = MaterialTheme.typography.bodyMedium, color = GrayText)
                }
            }
        }

        // KPI Summary
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BentoCard(modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = Emerald.copy(alpha = 0.3f), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Total Export Revenue", style = MaterialTheme.typography.labelMedium, color = GrayText)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("₹2,10,70,000", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Emerald)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.ArrowUpward, contentDescription = null, tint = Emerald, modifier = Modifier.size(14.dp))
                        Text(" 18.4% YoY", style = MaterialTheme.typography.labelMedium, color = Emerald)
                    }
                }
                BentoCard(modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.TrendingDown, contentDescription = null, tint = ElectricBlue.copy(alpha = 0.3f), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Avg Order Value", style = MaterialTheme.typography.labelMedium, color = GrayText)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("₹3,88,500", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = ElectricBlue)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.ArrowUpward, contentDescription = null, tint = Emerald, modifier = Modifier.size(14.dp))
                        Text(" 6.2% MoM", style = MaterialTheme.typography.labelMedium, color = Emerald)
                    }
                }
            }
        }

        // Revenue Trend Chart (Canvas-based line chart)
        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                Text("Revenue Trend (Last 7 Months)", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                Spacer(modifier = Modifier.height(16.dp))
                RevenueLineChart(data = monthlyData)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    monthlyData.forEach { d ->
                        Text(d.month, style = MaterialTheme.typography.labelMedium, color = GrayText)
                    }
                }
            }
        }

        // Top Products
        item {
            Text("Top Export Products", style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onBackground)
        }

        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                topProducts.forEachIndexed { idx, product ->
                    if (idx > 0) Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text(product.category, style = MaterialTheme.typography.labelMedium, color = GrayText)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("₹${String.format("%,.0f", product.revenue)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = ElectricBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { product.share },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = if (idx == 0) Emerald else ElectricBlue,
                        trackColor = ElectricBlue.copy(alpha = 0.1f)
                    )
                }
            }
        }

        // Destination Markets
        item {
            Text("Destination Markets", style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onBackground)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                destinationMarkets.forEach { market ->
                    BentoCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(market.flag, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(market.country, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                    Text("${market.shipments} shipments", style = MaterialTheme.typography.labelMedium, color = GrayText)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("₹${String.format("%,.0f", market.revenue)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Emerald
                                )
                                StatusBadge("Active", BadgeType.SUCCESS)
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun RevenueLineChart(data: List<MonthlyRevenue>) {
    val maxVal = data.maxOf { it.amount }
    val lineColor = ElectricBlue
    val fillColor = ElectricBlue.copy(alpha = 0.1f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        val width = size.width
        val height = size.height
        val stepX = width / (data.size - 1)

        val points = data.mapIndexed { i, d ->
            Offset(i * stepX, height - (d.amount / maxVal * height).toFloat())
        }

        // Fill path
        val fillPath = Path().apply {
            moveTo(points.first().x, height)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, height)
            close()
        }
        drawPath(fillPath, color = fillColor)

        // Line
        val linePath = Path().apply {
            points.forEachIndexed { i, p ->
                if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
            }
        }
        drawPath(linePath, color = lineColor, style = Stroke(width = 4f, cap = StrokeCap.Round))

        // Dots
        points.forEach { p ->
            drawCircle(color = lineColor, radius = 6f, center = p)
            drawCircle(color = Color.White, radius = 3f, center = p)
        }
    }
}
