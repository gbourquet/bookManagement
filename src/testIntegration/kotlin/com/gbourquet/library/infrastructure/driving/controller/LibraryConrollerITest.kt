package com.gbourquet.library.infrastructure.driving.controller

import com.gbourquet.library.domain.model.Book
import com.gbourquet.library.domain.usecase.LibraryUseCase
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

@WebMvcTest
class LibraryConrollerITest(
    @MockkBean private val libraryUseCase: LibraryUseCase,
    private val mockMvc: MockMvc
) : StringSpec({
    extension(SpringExtension)

    "rest route get books" {
        // GIVEN
        every { libraryUseCase.getBooks() } returns listOf(Book(1,"A", "B", false))

        // WHEN
        mockMvc.get("/books")
            //THEN
            .andExpect {
                status { isOk() }
                content { content { APPLICATION_JSON } }
                content {
                    json(
                        // language=json
                        """
                        [
                          {
                            "title": "A",
                            "author": "B"
                          }
                        ]
                        """.trimIndent()
                    )
                }
            }
    }

    "rest route post book" {
        every { libraryUseCase.addBook(any(), any()) } returns Book(0L,"Les misérables", "Victor Hugo", false)

        mockMvc.post("/books") {
            // language=json
            content = """
                {
                  "title": "Les misérables",
                  "author": "Victor Hugo"
                }
            """.trimIndent()
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
        }

        val expectedTitle = "Les misérables"
        val expectedAuthor = "Victor Hugo"

        verify(exactly = 1) { libraryUseCase.addBook(expectedTitle, expectedAuthor) }
    }

    "rest route post book should return 400 when body is not good" {
        every { libraryUseCase.addBook(any(), any()) } returns Book(0L,"Les misérables", "Victor Hugo", false)

        mockMvc.post("/books") {
            // language=json
            content = """
                {
                  "name": "Les misérables",
                  "author": "Victor Hugo"
                }
            """.trimIndent()
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 0) { libraryUseCase.addBook(any(), any()) }
    }

    "rest route patch book to reserve a book" {
        justRun { libraryUseCase.reserveBook(any()) }

        mockMvc.patch("/books/1") {
            // language=json
            content = ""
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        verify(exactly = 1) { libraryUseCase.reserveBook(1) }
    }

    "rest route patch book should return 400 when book is already reserved" {
        every { libraryUseCase.reserveBook(any()) }.throws(IllegalStateException("Book already reserved"))

        mockMvc.patch("/books/1") {
            // language=json
            content = ""
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 1) { libraryUseCase.reserveBook(1) }
    }
})