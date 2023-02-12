package com.luis.montes.ubicaciones.utilidades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/** Actualiza el MutableLiveData.value automaticamente usando la funcion especifica de su valor. **/
inline fun <T> MutableLiveData<T>.update(function: (T?) -> T?) {
    val newValue = function(value)
    this.value = newValue
}

/** Representa este mutablelivedata as Livedata. **/
fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this
}