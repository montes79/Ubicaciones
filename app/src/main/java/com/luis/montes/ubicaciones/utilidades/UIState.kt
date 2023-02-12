package com.luis.montes.ubicaciones.utilidades

sealed class UIState<out T> {
    object InitialState : UIState<Nothing>()
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error<out T>(val error: String, val exception: String = "") : UIState<T>()
}
