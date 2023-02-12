package com.luis.montes.ubicaciones.data.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luis.montes.ubicaciones.casosdeuso.RecetasRecuperarCasoUso
import com.luis.montes.ubicaciones.data.viewmodels.RecetasViewModel

@Suppress("UNCHECKED_CAST")
class RecetasVMFactory (
    private val casoUsoRecuperarCasoUso: RecetasRecuperarCasoUso
        ):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RecetasViewModel::class.java)){
            return RecetasViewModel(casoUsoRecuperarCasoUso) as T
        }

        throw IllegalArgumentException("Algo sucedio con la factory del vm")
        //return super.create(modelClass)
    }
}