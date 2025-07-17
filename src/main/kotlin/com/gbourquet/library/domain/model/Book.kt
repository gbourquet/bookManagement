package com.gbourquet.library.domain.model

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val reserved: Boolean = false
) {
    init {
        require(title.isNotBlank()) { "Title must not be blank" }
        require(author.isNotBlank()) { "Author must not be blank" }
    }
}