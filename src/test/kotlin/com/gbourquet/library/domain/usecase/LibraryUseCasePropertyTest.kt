package com.gbourquet.library.domain.usecase

import com.gbourquet.library.domain.model.Book
import com.gbourquet.library.domain.port.BookRepositoryPort
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.property.Arb
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.checkAll

class InMemoryBookRepositoryPort : BookRepositoryPort {
    private val books = mutableListOf<Book>()

    override fun getBooks(): List<Book> = books
    override fun getBook(bookId: Long): Book {
        TODO("Not yet implemented")
    }

    override fun updateBook(book: Book) {
        TODO("Not yet implemented")
    }

    override fun addBook(title: String, author: String) : Book {
        return Book(0, title, author, false).also { book -> books.add(book) }
    }

    fun clear() {
        books.clear()
    }
}

class LibraryUseCasePropertyTest : StringSpec({

    val bookRepository = InMemoryBookRepositoryPort()
    val libraryUseCase = LibraryUseCase(bookRepository)

    "should return all elements in the alphabetical order" {
        bookRepository.clear()
        val titles = mutableListOf<String>()
        checkAll(Arb.stringPattern("""[A-Za-z]{1,10}""")) { title ->
            titles.add(title)
            libraryUseCase.addBook(title, "Isaac Asimov")
        }

        val res = libraryUseCase.getBooks()
        res.map { it.title } shouldContainExactly titles.sorted()

    }
})