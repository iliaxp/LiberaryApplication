package com.iliaxp.liberaryapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iliaxp.liberaryapplication.model.Category

@Composable
fun CategoryButtons(
    selectedCategorys: Category,
    onCategorySelected: (Category) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(Category.values()) { category ->
            FilterChip(
                selected = category == selectedCategorys,
                onClick = { onCategorySelected(category) },
                label = { Text(category.name) }
            )
        }
    }
} 