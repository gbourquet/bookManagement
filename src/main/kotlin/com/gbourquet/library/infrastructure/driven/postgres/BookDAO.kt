package com.gbourquet.library.infrastructure.driven.postgres

import com.gbourquet.library.domain.model.Book
import com.gbourquet.library.domain.port.BookRepositoryPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

@Repository
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BookRepositoryPort {
    override fun getBooks(): List<Book> {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM BOOK", MapSqlParameterSource()) { rs, _ ->
                Book(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    author = rs.getString("author"),
                    reserved = rs.getBoolean("reserved")
                )
            }
    }

    override fun getBook(bookId: Long): Book? {
        return namedParameterJdbcTemplate
            .queryForObject(
                "SELECT * FROM BOOK WHERE id = :id",
                mapOf("id" to bookId)
            ) { rs, _ ->
                Book(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    author = rs.getString("author"),
                    reserved = rs.getBoolean("reserved")
                )
            }
    }

    override fun addBook(title: String, author: String) : Book {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(
            "INSERT INTO BOOK(title, author, reserved) values (:title, :author, :reserved)",
            MapSqlParameterSource().apply {
                addValue("title", title)
                addValue("author", author)
                addValue("reserved", false)
            },
            keyHolder,
            arrayOf("id")
        )

        val bookId = keyHolder.key
        check(bookId != null)

        return Book(bookId.toLong(),
            title,
            author,
            false)
    }

    override fun updateBook(book: Book) {
        namedParameterJdbcTemplate
            .update(
                "UPDATE BOOK SET title = :title, author = :author, reserved = :reserved WHERE id = :id",
                mapOf(
                    "id" to book.id,
                    "title" to book.title,
                    "author" to book.author,
                    "reserved" to book.reserved
                )
            )
    }
}