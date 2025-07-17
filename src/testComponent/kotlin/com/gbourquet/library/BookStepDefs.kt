package com.gbourquet.library

import com.gbourquet.library.infrastructure.driving.controller.dto.BookDTO
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.text.equals


class BookStepDefs {
    @LocalServerPort
    private val port: Int? = 0

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Before
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Before
    fun cleanDatabase() {
        jdbcTemplate.update("DELETE FROM BOOK")
    }

    @When("the user creates the book {string} written by {string}")
    fun createBook(title: String, author: String) {
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "title": "$title",
                      "author": "$author"
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getObject("", BookDTO::class.java)
            .copy(id=-1L)
            .shouldBe(BookDTO(-1L, title, author, false))
    }

    @When("the user get all books")
    fun getAllBooks() {
        lastBookResult = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @When("the user reserve the book {string}")
    fun reserveBook(title: String) {
        val bookId = jdbcTemplate.queryForObject("SELECT id FROM BOOK WHERE title = ?", Int::class.java, title)
        given()
            .contentType(ContentType.JSON)
            .and()
            .`when`()
            .patch("/books/${bookId}")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Then("the list should contains the following books in the same order")
    fun shouldHaveListOfBooks(payload: List<Map<String, String>>) {
        val expectedBooks = payload.map { row ->
            BookDTO(
                id = -1L,
                title = row["title"] as String,
                author = row["author"] as String,
                reserved = row["reserved"].equals("true")
            )
        }

        val books = lastBookResult
            .extract()
            .body()
            .jsonPath()
            .getList("", BookDTO::class.java)
            .map { book -> book.copy(id=-1L) }

        books shouldBe expectedBooks

    }

    companion object {
        lateinit var lastBookResult: ValidatableResponse
    }
}