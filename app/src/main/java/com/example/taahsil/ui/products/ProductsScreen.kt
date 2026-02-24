package com.example.taahsil.ui.products

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddShoppingCart
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taahsil.data.local.entity.ProductEntity
import com.example.taahsil.ui.components.BadgeType
import com.example.taahsil.ui.components.BentoCard
import com.example.taahsil.ui.components.StatusBadge
import com.example.taahsil.ui.orders.CartItem
import com.example.taahsil.ui.orders.OrderViewModel
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.BorderLight
import com.example.taahsil.ui.theme.Emerald
import com.example.taahsil.ui.theme.GrayText

@Composable
fun ProductsScreen(
    onProductClick: (ProductEntity) -> Unit = {},
    viewModel: ProductViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Products",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Browse catalog by name, category, or HS code",
            style = MaterialTheme.typography.bodyMedium,
            color = GrayText
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by name, HSCode, category...") },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search") },
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricBlue,
                unfocusedBorderColor = BorderLight,
                cursorColor = ElectricBlue
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Products Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.products) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product) },
                    onAddToCart = {
                        orderViewModel.addToCart(
                            CartItem(
                                productId = product.productId,
                                productName = product.productName,
                                unitPrice = product.unitPrice,
                                quantity = 1
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductEntity,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    BentoCard(onClick = onClick) {
        Icon(
            Icons.Rounded.Category,
            contentDescription = null,
            tint = ElectricBlue.copy(alpha = 0.3f),
            modifier = Modifier.height(28.dp).width(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.productName,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        StatusBadge(text = product.category, type = BadgeType.INFO)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "HS: ${product.hsCode}",
                    style = MaterialTheme.typography.labelMedium,
                    color = GrayText
                )
                Text(
                    text = "${product.weight} kg",
                    style = MaterialTheme.typography.labelMedium,
                    color = GrayText
                )
            }
            Text(
                text = "₹${String.format("%,.0f", product.unitPrice)}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = ElectricBlue
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onAddToCart,
            modifier = Modifier.fillMaxWidth().height(36.dp),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(containerColor = Emerald),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
        ) {
            Icon(
                Icons.Rounded.AddShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add to Cart", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
