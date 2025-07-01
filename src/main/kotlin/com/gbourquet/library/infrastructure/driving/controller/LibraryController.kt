package com.gbourquet.library.infrastructure.driving.controller

import com.gbourquet.library.domain.usecase.LibraryUseCase
import com.gbourquet.library.infrastructure.driving.controller.dto.BookDTO
import com.gbourquet.library.infrastructure.driving.controller.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class LibraryController(
    private val libraryUseCase: LibraryUseCase
) {
    @CrossOrigin
    @GetMapping
    fun getAllBooks(): List<BookDTO> {
        return libraryUseCase.getBooks()
            .map { it.toDto() }
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody bookDTO: BookDTO) {
        libraryUseCase.addBook(bookDTO.toDomain())
    }

}