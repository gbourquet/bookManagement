package com.gbourquet.library.domain.port

import com.gbourquet.library.domain.model.Book

interface BookRepositoryPort {
    fun addBook(book: Book)
    fun getBooks(): List<Book>
}