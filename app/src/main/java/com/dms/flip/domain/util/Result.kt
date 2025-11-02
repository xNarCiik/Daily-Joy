package com.dms.flip.domain.util

sealed interface Result<out T> {
    data class Ok<T>(val data: T) : Result<T>
    data class Err(val throwable: Throwable) : Result<Nothing>
}
