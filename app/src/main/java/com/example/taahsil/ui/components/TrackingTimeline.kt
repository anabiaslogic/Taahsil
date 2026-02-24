package com.example.taahsil.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.taahsil.ui.theme.ElectricBlue
import com.example.taahsil.ui.theme.Emerald
import com.example.taahsil.ui.theme.GrayText

data class TrackingStep(
    val title: String,
    val subtitle: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean = false
)

@Composable
fun TrackingTimeline(
    steps: List<TrackingStep>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Dot and line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(32.dp)
                ) {
                    val dotColor = when {
                        step.isCompleted -> Emerald
                        step.isCurrent -> ElectricBlue
                        else -> GrayText
                    }
                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawCircle(
                            color = dotColor,
                            radius = if (step.isCurrent) 8.dp.toPx() else 6.dp.toPx()
                        )
                        if (step.isCompleted || step.isCurrent) {
                            drawCircle(color = Color.White, radius = 3.dp.toPx())
                        }
                    }
                    if (index < steps.size - 1) {
                        Canvas(
                            modifier = Modifier
                                .width(2.dp)
                                .height(40.dp)
                        ) {
                            drawLine(
                                color = if (step.isCompleted) Emerald else GrayText.copy(alpha = 0.3f),
                                start = Offset(size.width / 2, 0f),
                                end = Offset(size.width / 2, size.height),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Content
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (step.isCompleted || step.isCurrent)
                            MaterialTheme.colorScheme.onSurface
                        else GrayText
                    )
                    Text(
                        text = step.subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = GrayText
                    )
                    if (index < steps.size - 1) {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}
