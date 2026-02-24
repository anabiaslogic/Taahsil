package com.example.taahsil.ui.currency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taahsil.ui.components.BentoCard
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.Emerald
import com.example.taahsil.ui.theme.GrayText
import com.example.taahsil.ui.theme.BorderLight

// Approximate static exchange rates to INR (as of early 2025)
val exchangeRates = mapOf(
    "INR (₹)" to 1.0,
    "USD ($)" to 83.12,
    "EUR (€)" to 89.45,
    "GBP (£)" to 104.82,
    "AED (د.إ)" to 22.62,
    "SGD (S$)" to 61.34,
    "JPY (¥)" to 0.55,
    "CNY (¥)" to 11.49,
    "AUD (A$)" to 53.71,
    "CAD (C$)" to 60.23,
    "SAR (﷼)" to 22.15
)

// Key trade corridors for India
data class TradeRate(val country: String, val currency: String, val flag: String, val rateToInr: Double)

val tradeCorridors = listOf(
    TradeRate("United States", "USD", "\uD83C\uDDFA\uD83C\uDDF8", 83.12),
    TradeRate("UAE (Dubai)", "AED", "\uD83C\uDDE6\uD83C\uDDEA", 22.62),
    TradeRate("Germany", "EUR", "\uD83C\uDDE9\uD83C\uDDEA", 89.45),
    TradeRate("United Kingdom", "GBP", "\uD83C\uDDEC\uD83C\uDDE7", 104.82),
    TradeRate("Singapore", "SGD", "\uD83C\uDDF8\uD83C\uDDEC", 61.34),
    TradeRate("Japan", "JPY", "\uD83C\uDDEF\uD83C\uDDF5", 0.55),
    TradeRate("China", "CNY", "\uD83C\uDDE8\uD83C\uDDF3", 11.49),
    TradeRate("Australia", "AUD", "\uD83C\uDDE6\uD83C\uDDFA", 53.71),
    TradeRate("Saudi Arabia", "SAR", "\uD83C\uDDF8\uD83C\uDDE6", 22.15)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen() {
    var amount by remember { mutableStateOf("10000") }
    var fromCurrency by remember { mutableStateOf("INR (₹)") }
    var toCurrency by remember { mutableStateOf("USD ($)") }
    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    val currencies = exchangeRates.keys.toList()

    fun convertAmount(): String {
        val value = amount.toDoubleOrNull() ?: return "—"
        val fromRate = exchangeRates[fromCurrency] ?: 1.0
        val toRate = exchangeRates[toCurrency] ?: 1.0
        val inrValue = value * fromRate
        val result = inrValue / toRate
        return String.format("%,.2f", result)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.CurrencyExchange,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(28.dp)
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(10.dp))
                Column {
                    Text(
                        text = "Currency Converter",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 26.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Live trade currency reference rates",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText
                    )
                }
            }
        }

        // Converter Card
        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                // Amount input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = BorderLight,
                        cursorColor = ElectricBlue
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // From / To row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // FROM dropdown
                    ExposedDropdownMenuBox(
                        expanded = fromExpanded,
                        onExpandedChange = { fromExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = fromCurrency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("From") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderLight
                            )
                        )
                        ExposedDropdownMenu(expanded = fromExpanded, onDismissRequest = { fromExpanded = false }) {
                            currencies.forEach { cur ->
                                DropdownMenuItem(text = { Text(cur) }, onClick = {
                                    fromCurrency = cur; fromExpanded = false
                                })
                            }
                        }
                    }

                    Icon(
                        Icons.Rounded.SwapHoriz,
                        contentDescription = "Swap",
                        tint = ElectricBlue,
                        modifier = Modifier.size(28.dp)
                    )

                    // TO dropdown
                    ExposedDropdownMenuBox(
                        expanded = toExpanded,
                        onExpandedChange = { toExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = toCurrency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("To") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderLight
                            )
                        )
                        ExposedDropdownMenu(expanded = toExpanded, onDismissRequest = { toExpanded = false }) {
                            currencies.forEach { cur ->
                                DropdownMenuItem(text = { Text(cur) }, onClick = {
                                    toCurrency = cur; toExpanded = false
                                })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Result
                BentoCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Converted Amount", style = MaterialTheme.typography.labelMedium, color = GrayText)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = convertAmount(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Emerald
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$toCurrency  ·  Rate: 1 ${fromCurrency.substringBefore(" ")} = ${
                            String.format("%.4f", (exchangeRates[fromCurrency] ?: 1.0) / (exchangeRates[toCurrency] ?: 1.0))
                        } ${toCurrency.substringBefore(" ")}",
                        style = MaterialTheme.typography.labelMedium,
                        color = GrayText
                    )
                }
            }
        }

        // Trade Corridors
        item {
            Text(
                text = "India's Key Trade Corridors",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tradeCorridors.forEach { corridor ->
                    BentoCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(corridor.flag, fontSize = 24.sp)
                                Spacer(modifier = Modifier.size(10.dp))
                                Column {
                                    Text(
                                        corridor.country,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        corridor.currency,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = GrayText
                                    )
                                }
                            }
                            Text(
                                text = "₹${String.format("%,.2f", corridor.rateToInr)}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = ElectricBlue
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
