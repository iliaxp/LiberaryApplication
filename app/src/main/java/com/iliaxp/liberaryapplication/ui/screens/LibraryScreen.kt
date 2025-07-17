package com.iliaxp.liberaryapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iliaxp.liberaryapplication.model.Book
import com.iliaxp.liberaryapplication.ui.components.BookCard
import com.iliaxp.liberaryapplication.ui.components.BookFilterDropdown
import com.iliaxp.liberaryapplication.ui.components.BookSortOption
import com.iliaxp.liberaryapplication.ui.components.CategoryButtons
import com.iliaxp.liberaryapplication.ui.components.ImageSlider
import com.iliaxp.liberaryapplication.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onCartClick: () -> Unit,
    onBookClick: (Book) -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val filteredBooks by viewModel.filteredBooks.collectAsState()
    var selectedSortOption by remember { mutableStateOf(BookSortOption.MOST_POPULAR) }
    val lazyListState = rememberLazyListState()

    // Sort books based on selected option
    val sortedBooks = remember(filteredBooks, selectedSortOption) {
        when (selectedSortOption) {
            BookSortOption.MOST_POPULAR -> filteredBooks.sortedByDescending { it.rating }
            BookSortOption.TITLE -> filteredBooks.sortedBy { it.name }
            BookSortOption.PRICE_LOW_TO_HIGH -> filteredBooks.sortedBy { it.price }
            BookSortOption.PRICE_HIGH_TO_LOW -> filteredBooks.sortedByDescending { it.price }
            BookSortOption.RATING -> filteredBooks.sortedByDescending { it.rating }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = isSearchActive,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = { Text("Search books...") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    viewModel.updateSearchQuery(searchQuery)
                                }
                            ),
                            trailingIcon = {
                                IconButton(onClick = { viewModel.setSearchActive(false) }) {
                                    Icon(Icons.Default.Close, contentDescription = "Close search")
                                }
                            }
                        )
                    }
                    AnimatedVisibility(
                        visible = !isSearchActive,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        Text("Mahdi Shelf")
                    }
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(onClick = { viewModel.setSearchActive(true) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        Box {
                            IconButton(onClick = onCartClick) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            }
                            if (cartItems.isNotEmpty()) {
                                Badge(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                ) {
                                    Text(cartItems.size.toString())
                                }
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Slider
            item {
                ImageSlider(
                    images = listOf(
                        "https://s33.picofile.com/file/8484992450/432.png",
                        "https://s33.picofile.com/file/8484992534/421.png",
                        "https://s33.picofile.com/file/8484992584/455.png"
                    ),
                    modifier = Modifier.padding(16.dp),
                    lazyListState = lazyListState
                )
            }

            // Categorys Buttons
            item {
                CategoryButtons(
                    selectedCategorys = selectedCategory,
                    onCategorySelected = { viewModel.setSelectedCategory(it) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Books Header with Filter
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Books:",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    
                    BookFilterDropdown(
                        selectedOption = selectedSortOption,
                        onOptionSelected = { selectedSortOption = it },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Books Grid
            items(
                items = sortedBooks.chunked(2),
                key = { it.first().id }
            ) { rowBooks ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowBooks.forEach { book ->
                        BookCard(
                            book = book,
                            onBuyClick = { viewModel.addToCart(book) },
                            onBookClick = { onBookClick(book) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Add empty space if the row has only one book
                    if (rowBooks.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

