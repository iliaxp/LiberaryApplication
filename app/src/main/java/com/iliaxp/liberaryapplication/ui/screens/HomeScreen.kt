package com.iliaxp.liberaryapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.iliaxp.liberaryapplication.model.Category
import com.iliaxp.liberaryapplication.viewmodel.LibraryViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: LibraryViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        CategorySection(
            onCategoryClick = { category ->
                viewModel.setSelectedCategory(category)
                navController.navigate("library") {
                    popUpTo("library") { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun CategorySection(
    onCategoryClick: (Category) -> Unit
) {
    Category.values().forEach { category ->
        Button(
            onClick = { onCategoryClick(category) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text = category.name.replace("_", " "))
        }
    }
} 