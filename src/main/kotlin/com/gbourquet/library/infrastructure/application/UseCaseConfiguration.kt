package com.gbourquet.library.infrastructure.application

import com.gbourquet.library.domain.port.BookRepositoryPort
import com.gbourquet.library.domain.usecase.LibraryUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {
    @Bean
    fun libraryUseCase(bookDAO: BookRepositoryPort): LibraryUseCase {
        return LibraryUseCase(bookDAO)
    }
}