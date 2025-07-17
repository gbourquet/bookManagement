package com.gbourquet.library.infrastructure.driving.controller.dto

import com.gbourquet.library.domain.model.Book

data class BookDTO(val id: Long, val title: String, val author: String, val reserved: Boolean) {
    fun toDomain(): Book {
        return Book(
            id = this.id,
            title = this.title,
            author = this.author,
            reserved = false
        )
    }
}

fun Book.toDto() = BookDTO(
    id = this.id,
    title = this.title,
    author = this.author,
    reserved = this.reserved
)