package com.gbourquet.library.domain.port

import com.gbourquet.library.domain.model.Book

interface BookRepositoryPort {
    fun addBook(title: String, author: String) : Book
    fun getBooks(): List<Book>
    fun getBook(bookId: Long) : Book?
    fun updateBook(book: Book)
}