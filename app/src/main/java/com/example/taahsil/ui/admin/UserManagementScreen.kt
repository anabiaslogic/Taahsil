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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.taahsil.data.local.entity.UserEntity
import com.example.taahsil.ui.components.BadgeType
import com.example.taahsil.ui.components.BentoCard
import com.example.taahsil.ui.components.StatusBadge
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.GrayText

@Composable
fun UserManagementScreen(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.Group,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "User Management",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${state.users.size} registered users",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Role summary cards
        item {
            val admins = state.users.count { it.role == "Admin" }
            val staff = state.users.count { it.role == "Staff" }
            val customers = state.users.count { it.role == "Customer" }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BentoCard(modifier = Modifier.weight(1f)) {
                    Text("Admins", style = MaterialTheme.typography.labelMedium, color = GrayText)
                    Text(
                        "$admins",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = ElectricBlue
                    )
                }
                BentoCard(modifier = Modifier.weight(1f)) {
                    Text("Staff", style = MaterialTheme.typography.labelMedium, color = GrayText)
                    Text(
                        "$staff",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = ElectricBlue
                    )
                }
                BentoCard(modifier = Modifier.weight(1f)) {
                    Text("Customers", style = MaterialTheme.typography.labelMedium, color = GrayText)
                    Text(
                        "$customers",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = ElectricBlue
                    )
                }
            }
        }

        if (state.users.isEmpty()) {
            item {
                BentoCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Rounded.Group, contentDescription = null, tint = GrayText, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No users registered yet", color = GrayText, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        items(state.users) { user ->
            UserCard(user = user)
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun UserCard(user: UserEntity) {
    BentoCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.labelMedium,
                        color = GrayText
                    )
                    if (user.companyName.isNotBlank()) {
                        Text(
                            text = "${user.companyName} · ${user.country}",
                            style = MaterialTheme.typography.labelMedium,
                            color = GrayText
                        )
                    }
                }
            }
            StatusBadge(
                text = user.role,
                type = when (user.role) {
                    "Admin" -> BadgeType.ERROR
                    "Staff" -> BadgeType.INFO
                    else -> BadgeType.SUCCESS
                }
            )
        }
    }
}
