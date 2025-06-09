package com.iliaxp.liberaryapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iliaxp.liberaryapplication.model.Book
import com.iliaxp.liberaryapplication.ui.screens.BookDetailScreen
import com.iliaxp.liberaryapplication.ui.screens.BookDetailsScreen
import com.iliaxp.liberaryapplication.ui.screens.CartScreen
import com.iliaxp.liberaryapplication.ui.screens.LibraryScreen
import com.iliaxp.liberaryapplication.ui.screens.PaymentScreen
import com.iliaxp.liberaryapplication.ui.screens.RegisterScreen
import com.iliaxp.liberaryapplication.ui.screens.SplashScreen
import com.iliaxp.liberaryapplication.ui.screens.WelcomeScreen
import com.iliaxp.liberaryapplication.ui.theme.LibraryApplicationTheme
import com.iliaxp.liberaryapplication.viewmodel.LibraryViewModel
import com.iliaxp.liberaryapplication.viewmodel.LibraryViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the saved state from SharedPreferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val hasCompletedOnboarding = sharedPreferences.getBoolean("has_completed_onboarding", false)

        setContent {
            LibraryApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by remember { mutableStateOf(!hasCompletedOnboarding) }
                    var showWelcome by remember { mutableStateOf(!hasCompletedOnboarding) }
                    var selectedBook by remember { mutableStateOf<Book?>(null) }
                    var showCart by remember { mutableStateOf(false) }
                    var showPayment by remember { mutableStateOf(false) }
                    val libraryViewModel: LibraryViewModel = viewModel(
                        factory = LibraryViewModelFactory(LocalContext.current)
                    )
                    val cartItems by libraryViewModel.cartItems.collectAsState()
                    val totalAmount = cartItems.fold(0.0) { acc, cartItem -> 
                        acc + (cartItem.book.price * cartItem.quantity) 
                    }

                    // Handle back press
                    val backHandler = remember {
                        object : OnBackPressedCallback(true) {
                            override fun handleOnBackPressed() {
                                when {
                                    showPayment -> {
                                        showPayment = false
                                    }
                                    showCart -> {
                                        showCart = false
                                    }
                                    selectedBook != null -> {
                                        selectedBook = null
                                    }
                                    else -> {
                                        // If we're on the main screen, let the system handle the back press
                                        isEnabled = false
                                        onBackPressedDispatcher.onBackPressed()
                                    }
                                }
                            }
                        }
                    }

                    DisposableEffect(Unit) {
                        onBackPressedDispatcher.addCallback(backHandler)
                        onDispose {
                            backHandler.remove()
                        }
                    }

                    // Show splash screen only on first launch
                    if (showSplash) {
                        SplashScreen(
                            onSplashFinished = {
                                showSplash = false
                            }
                        )
                    } else if (showWelcome) {
                        // Show welcome screen after splash
                        WelcomeScreen(
                            onGetStarted = {
                                // Save the state to SharedPreferences
                                sharedPreferences.edit().putBoolean("has_completed_onboarding", true).apply()
                                showWelcome = false
                            }
                        )
                    } else if (showPayment) {
                        // Show payment screen
                        PaymentScreen(
                            totalAmount = totalAmount,
                            onBackClick = {
                                showPayment = false
                            },
                            onPaymentComplete = {
                                libraryViewModel.clearCart()
                                showPayment = false
                                showCart = false
                                selectedBook = null
                            }
                        )
                    } else if (showCart) {
                        // Show cart screen
                        CartScreen(
                            cartItems = cartItems,
                            onBackClick = {
                                showCart = false
                            },
                            onCheckoutClick = {
                                showPayment = true
                            },
                            onQuantityChange = { cartItem, newQuantity ->
                                libraryViewModel.updateCartItemQuantity(cartItem, newQuantity)
                            },
                            onRemoveItem = { cartItem ->
                                libraryViewModel.removeFromCart(cartItem)
                            },
                            viewModel = libraryViewModel
                        )
                    } else if (selectedBook != null) {
                        // Show book details screen
                        BookDetailsScreen(
                            book = selectedBook!!,
                            onBackClick = {
                                selectedBook = null
                            },
                            onBuyClick = {
                                libraryViewModel.addToCart(selectedBook!!)
                                showCart = true
                            },
                            onCategoryClick = { category ->
                                libraryViewModel.setSelectedCategory(category)
                                selectedBook = null
                            }
                        )
                    } else {
                        // Main app content
                        LibraryScreen(
                            onCartClick = { showCart = true },
                            onBookClick = { book ->
                                selectedBook = book
                            },
                            viewModel = libraryViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryApp() {
    val navController = rememberNavController()
    val viewModel: LibraryViewModel = viewModel()
    var isFirstLaunch by remember { mutableStateOf(true) }

    if (isFirstLaunch) {
        SplashScreen(
            onSplashFinished = {
                isFirstLaunch = false
            }
        )
    } else {
        NavHost(navController = navController, startDestination = "register") {
            composable("register") {
                RegisterScreen(
                    onRegisterComplete = {
                        navController.navigate("library") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }

            composable("library") {
                LibraryScreen(
                    onBookClick = { bookId ->
                        navController.navigate("book_details/$bookId")
                    },
                    onCartClick = {
                        navController.navigate("cart")
                    },
                    viewModel = viewModel
                )
            }

            composable(
                route = "book_details/{bookId}",
                arguments = listOf(navArgument("bookId") { type = NavType.StringType })
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
                val book = viewModel.getBookById(bookId) ?: return@composable
                
                BookDetailScreen(
                    book = book,
                    onBackClick = { navController.popBackStack() },
                    onCategoryClick = { Category ->
                        navController.navigate("library") {
                            popUpTo("library") { inclusive = true }
                        }
                        viewModel.setSelectedCategory(Category)
                    },
                    onAddToCart = {
                        viewModel.addToCart(book)
                    },
                    onNavigateToCart = {
                        navController.navigate("cart")
                    }
                )
            }

            composable("cart") {
                val cartItems by viewModel.cartItems.collectAsState()
                CartScreen(
                    cartItems = cartItems,
                    onBackClick = { navController.popBackStack() },
                    onCheckoutClick = { navController.navigate("payment") },
                    onQuantityChange = { cartItem, newQuantity ->
                        viewModel.updateCartItemQuantity(cartItem, newQuantity)
                    },
                    onRemoveItem = { cartItem ->
                        viewModel.removeFromCart(cartItem)
                    },
                    viewModel = viewModel
                )
            }

            composable("payment") {
                PaymentScreen(
                    onBackClick = { navController.popBackStack() },
                    onPaymentComplete = {
                        viewModel.clearCart()
                        navController.navigate("library") {
                            popUpTo("library") { inclusive = true }
                        }
                    },
                    totalAmount = viewModel.getTotalPrice()
                )
            }
        }
    }
}