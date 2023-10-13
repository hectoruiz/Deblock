package com.hectoruiz.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorStateTest {

    @Test
    fun `errorState network empty`() {
        val errorState = ErrorState.NetworkError()

        assertEquals("", errorState.message)
    }

    @Test
    fun `errorState network message`() {
        val errorMessage = "Bad request"
        val errorState = ErrorState.NetworkError(errorMessage)

        assertEquals(errorMessage, errorState.message)
    }
}