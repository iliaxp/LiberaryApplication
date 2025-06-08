package com.iliaxp.liberaryapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iliaxp.liberaryapplication.R
import com.iliaxp.liberaryapplication.model.Book
import com.iliaxp.liberaryapplication.model.CartItem
import com.iliaxp.liberaryapplication.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(private val context: Context) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _books = MutableStateFlow(getSampleBooks())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _filteredBooks = MutableStateFlow<List<Book>>(emptyList())
    val filteredBooks: StateFlow<List<Book>> = _filteredBooks.asStateFlow()

    init {
        // Initialize filtered books with all books
        _filteredBooks.value = _books.value
    }

    fun setSelectedCategory(category: Category?) {
        _selectedCategory.value = category
        updateFilteredBooks()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateFilteredBooks()
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = ""
            updateFilteredBooks()
        }
    }

    fun updateFilteredBooks() {
        _filteredBooks.value = _books.value.filter { book ->
            val matchesCategory = _selectedCategory.value == null || book.category == _selectedCategory.value
            val searchQueryLower = _searchQuery.value.lowercase()
            val matchesSearch = _searchQuery.value.isEmpty() || 
                book.name.lowercase().contains(searchQueryLower) ||
                book.description.lowercase().contains(searchQueryLower) ||
                book.author.lowercase().contains(searchQueryLower)
            matchesCategory && matchesSearch
        }
    }

    fun addToCart(book: Book) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.book.id == book.id }

        if (existingItem != null) {
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentItems.add(CartItem(book, 1))
        }

        _cartItems.value = currentItems
    }

    fun removeFromCart(book: Book) {
        _cartItems.value = _cartItems.value.filter { it.book.id != book.id }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.fold(0.0) { acc, cartItem -> acc + cartItem.book.price }
    }

    fun getBookById(id: String): Book? {
        return _books.value.find { it.id == id }
    }

    private fun getSampleBooks(): List<Book> {
        return listOf(
            Book(
                id = "1",
                name = "Atomic Habits",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2020/04/Atomic-Habits.webp",
                price = 20,
                category = Category.ROMANCE,
                description = context.getString(R.string.book_great_gatsby_desc),
                author = "James Clear",
                rating = 4.5f
            ),
            Book(
                id = "2",
                name = "Animal Farm",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/Animal-Farm-3.jpg",
                price = 18,
                category = Category.ROMANCE,
                description = context.getString(R.string.book_mockingbird_desc),
                author = "Harper Lee",
                rating = 4.8f
            ),
            Book(
                id = "3",
                name = "1984 Nineteen Eighty Four",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/1984.jpg",
                price = 15,
                category = Category.ROMANCE,
                description = context.getString(R.string.book_1984_desc),
                author = "George Orwell",
                rating = 4.7f
            ),
            Book(
                id = "4",
                name = "Twilight",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2018/10/Twilight.jpg",
                price = 25,
                category = Category.HORROR,
                description = context.getString(R.string.book_hobbit_desc),
                author = "Stephenie Meyer",
                rating = 4.9f
            ),
            Book(
                id = "5",
                name = "The Shining",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2023/07/The-Shining.jpg",
                price = 4,
                category = Category.HORROR,
                description = context.getString(R.string.book_pride_prejudice_desc),
                author = "Jane Austen",
                rating = 4.6f
            ),
            Book(
                id = "6",
                name = "The Catcher in the Rye",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2023/07/The-Catcher-in-the-Rye-2.jpg",
                price = 9,
                category = Category.HORROR,
                description = context.getString(R.string.book_catcher_rye_desc),
                author = "J.D. Salinger",
                rating = 4.3f
            ),
            Book(
                id = "7",
                name = "The Hundred Year Old Man",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2023/07/Zamir.jpg",
                price = 21,
                category = Category.SCIENCE,
                description = context.getString(R.string.the_100_year_old),
                author = "Jonas Jonasson",
                rating = 4.3f
            ),
            Book(
                id = "8",
                name = "Zamir",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/The-Hundred-Year-Old-Man.jpg",
                price = 13,
                category = Category.SCIENCE,
                description = context.getString(R.string.zamir),
                author = "Hakan Günday",
                rating = 4.3f
            ),
            Book(
                id = "9",
                name = "The Good Terrorist",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/product_2672_1561485061_48202.jpg",
                price = 22,
                category = Category.SCIENCE,
                description = context.getString(R.string.the_good_terrorist),
                author = "Doris Lessing",
                rating = 4.3f
            )
            ,
            Book(
                id = "10",
                name = "Brief Answers to the Big Questions",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2019/07/600.jpg",
                price = 10,
                category = Category.FICTION,
                description = context.getString(R.string.brief_questions),
                author = "Stephen Hawking",
                rating = 4.3f
            ),
            Book(
                id = "11",
                name = "Powerless",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2024/06/Powerless.jpg",
                price = 10,
                category = Category.FICTION,
                description = context.getString(R.string.powerless),
                author = "Lauren Roberts",
                rating = 4.3f
            )
            ,
            Book(
                id = "12",
                name = "Crenshaw",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2023/12/Crenshaw.jpg",
                price = 8,
                category = Category.FICTION,
                description = context.getString(R.string.crenshaw),
                author = "Katherine Applegate",
                rating = 4.3f
            ),
            Book(
                id = "13",
                name = "Babel",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2023/08/Babel.jpg",
                price = 5,
                category = Category.HISTORY,
                description = context.getString(R.string.bable),
                author = "harper voyager",
                rating = 4.3f
            )
            ,
            Book(
                id = "14",
                name = "The Great Gatsby",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/the-Great-Gatsby-2.jpg",
                price = 7,
                category = Category.HISTORY,
                description = context.getString(R.string.the_grate_gasby),
                author = "Vintage Classics",
                rating = 4.3f
            )
            ,
            Book(
                id = "15",
                name = "The Giver of stars",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/jojo-moyes-the-giver-of-stars-1.jpg",
                price = 4,
                category = Category.HISTORY,
                description = context.getString(R.string.the_giver_of_stars),
                author = "penguin",
                rating = 4.3f
            ),
            Book(
                id = "16",
                name = "The Alchemist",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/the-Alchemist-1.jpg",
                price = 9,
                category = Category.DRAMA,
                description = context.getString(R.string.the_alchemist),
                author = "paulo coelho",
                rating = 4.3f
            ),
            Book(
                id = "17",
                name = "A Little Life",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2023/10/A-Little-Life.jpg",
                price = 15,
                category = Category.DRAMA,
                description = context.getString(R.string.a_little_life),
                author = "Random House Audio",
                rating = 4.3f
            ),
            Book(
                id = "18",
                name = "Hamlet",
                imageUrl = "https://zabanmehrpub.com/wp-content/uploads/2019/07/Hamlet.jpg",
                price = 12,
                category = Category.DRAMA,
                description = context.getString(R.string.halmet),
                author = "William Shakespeare",
                rating = 4.3f
            )

        )
    }
} 