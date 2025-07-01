package com.gbourquet.library.infrastructure.driven.postgres

import com.gbourquet.library.domain.model.Book
import com.gbourquet.library.domain.port.BookRepositoryPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BookRepositoryPort {
    override fun getBooks(): List<Book> {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM BOOK", MapSqlParameterSource()) { rs, _ ->
                Book(
                    title = rs.getString("title"),
                    author = rs.getString("author")
                )
            }
    }

    override fun addBook(book: Book) {
        namedParameterJdbcTemplate
            .update("INSERT INTO BOOK (title, author) values (:title, :author)", mapOf(
                "title" to book.title,
                "author" to book.author
            ))
    }
}