package com.example.taahsil.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taahsil.ui.theme.Emerald
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.ErrorRed

enum class BadgeType { SUCCESS, PENDING, ERROR, INFO }

@Composable
fun StatusBadge(
    text: String,
    type: BadgeType,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (type) {
        BadgeType.SUCCESS -> Color(0x1A10B981) to Emerald
        BadgeType.PENDING -> Color(0x1AF59E0B) to Color(0xFFF59E0B)
        BadgeType.ERROR -> Color(0x1AEF4444) to ErrorRed
        BadgeType.INFO -> Color(0x1A38BDF8) to ElectricBlue
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
