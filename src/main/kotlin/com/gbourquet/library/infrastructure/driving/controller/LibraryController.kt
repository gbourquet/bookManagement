package com.gbourquet.library.infrastructure.driving.controller

import com.gbourquet.library.domain.usecase.LibraryUseCase
import com.gbourquet.library.infrastructure.driving.controller.dto.BookDTO
import com.gbourquet.library.infrastructure.driving.controller.dto.BookInformationsDTO
import com.gbourquet.library.infrastructure.driving.controller.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
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
    fun addBook(@RequestBody newBookDTO: BookInformationsDTO) : BookDTO {
        println(newBookDTO)
        return libraryUseCase.addBook(newBookDTO.title, newBookDTO.author).toDto()
    }

    @CrossOrigin
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun reserveBook(@PathVariable id: Long) {
        libraryUseCase.reserveBook(id)
    }
}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to ex.message!!), HttpStatus.BAD_REQUEST)
    }
}