package com.gbourquet.library.domain.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class BookTest : StringSpec({
    "name is mandatory" {
        shouldThrow<IllegalArgumentException> { Book(1L,"", "Victor Hugo", false) }

    }

    "author is mandatory" {
        shouldThrow<IllegalArgumentException> { Book(1L,"Les robots", "", false) }
    }
})