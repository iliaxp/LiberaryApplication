package com.iliaxp.liberaryapplication.model

enum class Category {
    ALL,
    SCIENCE,
    FICTION,
    HORROR,
    HISTORY,
    ROMANCE,
    DRAMA,
    FANTASY,
    SCIENCE_FICTION;
    override fun toString(): String {
        return name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }
    }
} 