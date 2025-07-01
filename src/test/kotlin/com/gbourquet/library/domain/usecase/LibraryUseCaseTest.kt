package com.gbourquet.library.domain.usecase

import com.gbourquet.library.domain.model.Book
import com.gbourquet.library.domain.port.BookRepositoryPort
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class LibraryUseCaseTest : StringSpec({
    val bookRepository = mockk<BookRepositoryPort>()
    val libraryUseCase = LibraryUseCase(bookRepository)

    "getBooks returns sorted list of books" {
        // Arrange
        val books = listOf(
            Book("Les robots", "Isaac Asimov"),
            Book("Hypérion", "Dan Simons"),
            Book("Axiomatique", "Greg Egan")
        )
        every { bookRepository.getBooks() } returns books

        // Act
        val result = libraryUseCase.getBooks()

        // Assert
        result shouldContainExactly listOf(
            Book("Axiomatique", "Greg Egan"),
            Book("Hypérion", "Dan Simons"),
            Book("Les robots", "Isaac Asimov")
        )
    }

    "addBook adds a book to the repository" {
        // Arrange
        val book = Book("Les robots", "Isaac Asimov")
        justRun { bookRepository.addBook(any()) }

        // Act
        libraryUseCase.addBook(book)

        // Assert
        verify(exactly = 1) { bookRepository.addBook(book) }
    }})
