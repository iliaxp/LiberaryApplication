package com.iliaxp.liberaryapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    showRatingText: Boolean = true,
    starColor: Color = Color(0xFFFFD700), // Gold color
    starSize: Int = 20
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Display stars
        for (i in 1..5) {
            when {
                rating >= i -> {
                    // Full star
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Full star",
                        tint = starColor,
                        modifier = Modifier.size(starSize.dp)
                    )
                }
                rating > i - 1 && rating < i -> {
                    // Half star
                    Icon(
                        imageVector = Icons.Filled.StarHalf,
                        contentDescription = "Half star",
                        tint = starColor,
                        modifier = Modifier.size(starSize.dp)
                    )
                }
                else -> {
                    // Empty star
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Empty star",
                        tint = starColor.copy(alpha = 0.3f),
                        modifier = Modifier.size(starSize.dp)
                    )
                }
            }
        }

        // Display rating text if enabled
        if (showRatingText) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = String.format("%.1f", rating),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
} 