package com.gbourquet.library.domain.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class BookTest : StringSpec({
    "name is mandatory" {
        shouldThrow<IllegalArgumentException> { Book("", "Victor Hugo") }

    }

    "author is mandatory" {
        shouldThrow<IllegalArgumentException> { Book("Les robots", "") }
    }
})