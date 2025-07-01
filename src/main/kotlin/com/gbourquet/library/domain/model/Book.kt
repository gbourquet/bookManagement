package com.gbourquet.library.domain.model

data class Book(
    val title: String,
    val author: String
) {
    init {
        require(title.isNotBlank()) { "Title must not be blank" }
        require(author.isNotBlank()) { "Author must not be blank" }
    }
}