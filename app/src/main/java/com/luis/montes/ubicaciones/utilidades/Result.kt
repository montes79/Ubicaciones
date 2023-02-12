package com.luis.montes.ubicaciones.utilidades


sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val error: String = "",
        val exception: String = ""
    ) : Result<Nothing>()
}
