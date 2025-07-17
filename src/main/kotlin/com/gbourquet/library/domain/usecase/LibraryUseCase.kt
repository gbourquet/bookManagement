package com.gbourquet.library.domain.usecase

import com.gbourquet.library.domain.model.Book
import com.gbourquet.library.domain.port.BookRepositoryPort

class LibraryUseCase(val bookRepository: BookRepositoryPort) {
    fun getBooks() : List<Book> = bookRepository.getBooks().sortedBy { it.title }
    fun addBook(title: String, author: String) : Book {
        println(title)
        return bookRepository.addBook(title, author)
    }
    fun reserveBook(bookId: Long) {
        val book = bookRepository.getBook(bookId).also { book ->
            check (book != null) { "Book does not exist" }
            check (!book.reserved) { "Book already reserved" }
        }

        bookRepository.updateBook(book!!.copy(reserved = true))

    }
}