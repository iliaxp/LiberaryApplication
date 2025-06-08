package com.iliaxp.liberaryapplication.model



data class Book(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Int,
    val category: Category,
    val description: String,
    val author: String = "",
    val rating: Float = 0f,
    val reviews: Int = 0
)

data class CartItem(
    val book: Book,
    val quantity: Int = 1
)