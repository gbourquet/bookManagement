package com.gbourquet.library.infrastructure.driven.postgres

import com.gbourquet.library.domain.model.Book
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.ResultSet

@SpringBootTest
@ActiveProfiles("testIntegration")
class BookDAOITest(
    private val bookDAO: BookDAO
) : StringSpec() {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    init {
        extension(SpringExtension)

        beforeTest {
            jdbcTemplate.update (
                // language=sql
                "DELETE FROM book"
            )
        }

        "get all books from db" {
            // GIVEN
            jdbcTemplate.update(
                // language=sql
                """
               insert into book (id, title, author, reserved)
               values 
                   (1, 'Hamlet', 'Shakespeare', false),
                   (2, 'Les fleurs du mal', 'Beaudelaire', false),
                   (3, 'Harry Potter', 'Rowling', true);
            """.trimIndent()
            )

            // WHEN
            val res = bookDAO.getBooks()

            // THEN
            res.shouldContainExactlyInAnyOrder(
                Book(1L,"Hamlet", "Shakespeare",false),
                Book(2L,"Les fleurs du mal", "Beaudelaire", false),
                Book(3L,"Harry Potter", "Rowling", true)
            )
        }

        "get one book from db" {
            // GIVEN
            jdbcTemplate.update(
                // language=sql
                """
               insert into book (id, title, author, reserved)
               values 
                   (1, 'Hamlet', 'Shakespeare', false),
                   (2, 'Les fleurs du mal', 'Beaudelaire', false),
                   (3, 'Harry Potter', 'Rowling', true);
            """.trimIndent()
            )

            // WHEN
            val res = bookDAO.getBook(1)

            // THEN
            res shouldBe Book(1,"Hamlet", "Shakespeare",false)
        }

        "create book in db" {
            // GIVEN
            val title = "Les misérables"
            val author = "Victor Hugo"

            // WHEN
            bookDAO.addBook(title, author)

            // THEN
            val res =  jdbcTemplate.query(
            // language=sql
            "SELECT * from book"
            ) { rs, rowNum -> rs.toMap() }

            res shouldHaveSize 1
            assertSoftly(res.first()) {
                this["id"].shouldNotBeNull().shouldBeInstanceOf<Int>()
                this["title"].shouldBe("Les misérables")
                this["author"].shouldBe("Victor Hugo")
                this["reserved"].shouldBe(false)
            }
        }

        "update book in db" {
            // GIVEN
            jdbcTemplate.update(
                // language=sql
                """
               insert into book (id, title, author, reserved)
               values 
                   (1, 'Hamlet', 'Shakespeare', false),
                   (2, 'Les fleurs du mal', 'Beaudelaire', false),
                   (3, 'Harry Potter', 'Rowling', false);
            """.trimIndent()
            )
            val book = Book(1,"Hamlet", "Shakespeare",true)

            // WHEN
           bookDAO.updateBook(book)

            // THEN
            val res = jdbcTemplate.query(
                // language=sql
                "SELECT * from book WHERE id = 1"
            ) { rs, rowNum -> rs.toMap()}

            assertSoftly(res.first()) {
                this["id"].shouldNotBeNull().shouldBeInstanceOf<Int>()
                this["title"].shouldBe("Hamlet")
                this["author"].shouldBe("Shakespeare")
                this["reserved"].shouldBe(true)
            }
        }

        afterSpec {
            container.stop()
        }
    }

    companion object {
        private val container = PostgreSQLContainer<Nothing>("postgres:13-alpine")

        init {
            container.start()
            System.setProperty("spring.datasource.url", container.jdbcUrl)
            System.setProperty("spring.datasource.username", container.username)
            System.setProperty("spring.datasource.password", container.password)
        }

        private fun ResultSet.toMap(): Map<String, Any> {
            val md = this.metaData
            val columns = md.columnCount
            val row: MutableMap<String, Any> = HashMap(columns)
            for (i in 1..columns) {
                row[md.getColumnName(i)] = this.getObject(i)
            }
            return row
        }
    }
}