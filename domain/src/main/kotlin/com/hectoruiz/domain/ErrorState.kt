package com.hectoruiz.domain

sealed class ErrorState {
    data object NoError : ErrorState()
    data class NetworkError(val message: String = "") : ErrorState()
}