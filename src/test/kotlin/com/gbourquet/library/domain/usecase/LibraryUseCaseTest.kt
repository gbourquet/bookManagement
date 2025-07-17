package com.gbourquet.library.domain.usecase

import com.gbourquet.library.domain.model.Book
import com.gbourquet.library.domain.port.BookRepositoryPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
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
            Book(1,"Les robots", "Isaac Asimov", false),
            Book(2,"Hypérion", "Dan Simons", false),
            Book(3,"Axiomatique", "Greg Egan", false)
        )
        every { bookRepository.getBooks() } returns books

        // Act
        val result = libraryUseCase.getBooks()

        // Assert
        result shouldContainExactly listOf(
            Book(3,"Axiomatique", "Greg Egan", false),
            Book(2,"Hypérion", "Dan Simons", false),
            Book(1,"Les robots", "Isaac Asimov", false)
        )
    }

    "addBook adds a book to the repository" {
        // Arrange
        val title = "Les robots"
        val author = "Isaac Asimov"
        every { bookRepository.addBook(any(), any()) } returns Book(1,title, author, false)

        // Act
        val book = libraryUseCase.addBook(title, author)

        // Assert
        verify(exactly = 1) { bookRepository.addBook(title, author) }
        book shouldBe Book(1, title, author, false)
    }

    "reserveBook reserve a book" {
        // Arrange
        val notReservedBook = Book(1, "Axiomatique", "Greg Egan", false)
        val reservedBook = Book(1, "Axiomatique", "Greg Egan", true)
        justRun {bookRepository.updateBook(any())}
        every { bookRepository.getBook(any()) } returns notReservedBook

        // Act
        libraryUseCase.reserveBook(notReservedBook.id)

        // Assert
        verify(exactly = 1) {bookRepository.updateBook(reservedBook)}
    }

    "reserveBook throw an exception when trying to reserve an already reserved book" {
        // Arrange
        val alreadyReservedBook = Book(1, "Axiomatique", "Greg Egan", true)
        every { bookRepository.getBook(any()) } returns alreadyReservedBook

        // Act, Assert
        shouldThrow<IllegalStateException> {libraryUseCase.reserveBook(alreadyReservedBook.id)}
            .message shouldBe "Book already reserved"

    }

})
