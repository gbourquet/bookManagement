package com.gbourquet.library.domain.usecase

import com.gbourquet.library.domain.model.Book
import com.gbourquet.library.domain.port.BookRepositoryPort

class LibraryUseCase(val bookRepository: BookRepositoryPort) {
    fun getBooks() : List<Book> = bookRepository.getBooks().sortedBy { it.title }
    fun addBook(book: Book) {
        bookRepository.addBook(book)
    }
}