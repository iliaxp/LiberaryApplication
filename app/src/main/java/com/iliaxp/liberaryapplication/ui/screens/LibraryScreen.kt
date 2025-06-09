package com.iliaxp.liberaryapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iliaxp.liberaryapplication.model.Book
import com.iliaxp.liberaryapplication.model.Category
import com.iliaxp.liberaryapplication.ui.components.BookCard
import com.iliaxp.liberaryapplication.ui.components.CategoryButtons
import com.iliaxp.liberaryapplication.ui.components.ImageSlider
import com.iliaxp.liberaryapplication.viewmodel.LibraryViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.iliaxp.liberaryapplication.ui.components.BookFilterDropdown
import com.iliaxp.liberaryapplication.ui.components.BookSortOption
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.rememberLazyListState

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

// Sample data for testing
private fun getSampleBooks(): List<Book> {
    return List(20) { index ->
        Book(
            id = "book_$index",
            name = "Book ${index + 1}",
            imageUrl = "https://img.freepik.com/free-photo/book-composition-with-open-book_23-2147690555.jpg",
            price = 20 + index,
            category = Category.values()[index % (Category.values().size - 1) + 1],
            description = "This is a sample description for Book ${index + 1}. It contains interesting details about the book's content and themes.",
            author = "Author ${index + 1}",
            rating = 4.5f,
            reviews = 100 + index
        )
    }
} 