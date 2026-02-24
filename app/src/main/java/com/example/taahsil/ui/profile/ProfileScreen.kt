package com.example.taahsil.ui.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taahsil.data.SessionManager
import com.example.taahsil.ui.auth.AuthViewModel
import com.example.taahsil.ui.components.BadgeType
import com.example.taahsil.ui.components.BentoCard
import com.example.taahsil.ui.components.StatusBadge
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.Emerald
import com.example.taahsil.ui.theme.ErrorRed
import com.example.taahsil.ui.theme.GrayText
import javax.inject.Inject

@Composable
fun ProfileScreen(
    sessionManager: SessionManager,
    onLogout: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Pull from session (always has data) or VM state
    val userName = state.user?.name ?: sessionManager.getUserName() ?: "Trade User"
    val userEmail = state.user?.email ?: state.email.ifBlank { "—" }
    val userRole = state.userRole ?: sessionManager.getUserRole() ?: "Customer"
    val company = state.user?.companyName ?: ""
    val contactPerson = state.user?.contactPerson ?: ""
    val country = state.user?.country ?: ""
    val userId = state.user?.userId ?: sessionManager.getUserId() ?: "—"

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Rounded.Logout, contentDescription = null, tint = ErrorRed) },
            title = { Text("Sign Out", style = MaterialTheme.typography.headlineSmall) },
            text = { Text("Are you sure you want to sign out of your account?",
                style = MaterialTheme.typography.bodyMedium, color = GrayText) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Sign Out", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                "Profile",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Account details & settings",
                style = MaterialTheme.typography.bodyMedium, color = GrayText)
        }

        // Avatar + Name card
        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar circle with initials
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(2).uppercase(),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            ),
                            color = ElectricBlue
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        StatusBadge(
                            text = userRole,
                            type = when (userRole) {
                                "Admin" -> BadgeType.ERROR
                                "Staff" -> BadgeType.INFO
                                else -> BadgeType.SUCCESS
                            }
                        )
                        if (company.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(company,
                                style = MaterialTheme.typography.labelMedium,
                                color = GrayText
                            )
                        }
                    }
                }
            }
        }

        // Contact Details
        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Contact Details",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProfileInfoRow(icon = Icons.Rounded.Person, label = "Full Name", value = userName)
                ProfileInfoRow(icon = Icons.Rounded.Email, label = "Email", value = userEmail.ifBlank { "—" })
                if (contactPerson.isNotBlank())
                    ProfileInfoRow(icon = Icons.Rounded.Phone, label = "Contact Person", value = contactPerson)
                if (country.isNotBlank())
                    ProfileInfoRow(icon = Icons.Rounded.LocationOn, label = "Country", value = country)
            }
        }

        // Business Details
        if (company.isNotBlank() || userRole == "Admin") {
            item {
                BentoCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Business Info",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (company.isNotBlank())
                        ProfileInfoRow(icon = Icons.Rounded.Business, label = "Company", value = company)
                    ProfileInfoRow(icon = Icons.Rounded.Badge, label = "User ID", value = userId.take(16))
                    ProfileInfoRow(
                        icon = if (userRole == "Admin") Icons.Rounded.AdminPanelSettings else Icons.Rounded.ManageAccounts,
                        label = "Role",
                        value = userRole
                    )
                }
            }
        }

        // Account permissions
        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Permissions",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                val permissions = when (userRole) {
                    "Admin" -> listOf(
                        "View & manage all users" to true,
                        "Add / edit products" to true,
                        "View all orders & shipments" to true,
                        "Access financial reports" to true,
                        "Export trade documents" to true
                    )
                    "Staff" -> listOf(
                        "View & manage all users" to false,
                        "Add / edit products" to true,
                        "View all orders & shipments" to true,
                        "Access financial reports" to false,
                        "Export trade documents" to true
                    )
                    else -> listOf(
                        "View & manage all users" to false,
                        "Add / edit products" to false,
                        "Place orders" to true,
                        "Track shipments" to true,
                        "View own payment history" to true
                    )
                }

                permissions.forEach { (perm, allowed) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.Shield,
                                contentDescription = null,
                                tint = if (allowed) Emerald else GrayText,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(perm, style = MaterialTheme.typography.bodyMedium,
                                color = if (allowed) MaterialTheme.colorScheme.onSurface else GrayText)
                        }
                        Text(
                            if (allowed) "✓" else "✗",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (allowed) Emerald else ErrorRed
                        )
                    }
                }
            }
        }

        // App info
        item {
            BentoCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Taahsil", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = ElectricBlue)
                        Text("Import · Export · Simplified",
                            style = MaterialTheme.typography.labelMedium, color = GrayText)
                    }
                    Text("v1.0.0", style = MaterialTheme.typography.labelMedium, color = GrayText)
                }
            }
        }

        // Logout button
        item {
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
            ) {
                Icon(Icons.Rounded.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Sign Out", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = GrayText)
            Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
        }
    }
}
