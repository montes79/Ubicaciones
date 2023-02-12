package com.luis.montes.ubicaciones.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.montes.ubicaciones.casosdeuso.RecetasRecuperarCasoUso
import com.luis.montes.ubicaciones.data.modelos.RespuestaServicioRecetas
import com.luis.montes.ubicaciones.data.modelos.peticion.PeticionServicioRecetas
import com.luis.montes.ubicaciones.utilidades.UIState
import com.luis.montes.ubicaciones.utilidades.asLiveData
import com.luis.montes.ubicaciones.utilidades.update
import kotlinx.coroutines.launch
import com.luis.montes.ubicaciones.utilidades.Result

class RecetasViewModel (
     private val recetasRecuperarCasoUso: RecetasRecuperarCasoUso
        ): ViewModel(){

      private var _loaderPantalla = MutableLiveData(false)
      val loaderPantalla = _loaderPantalla.asLiveData()

      private var preResultadoProceso = MutableLiveData<UIState<RespuestaServicioRecetas>>(UIState.InitialState)
      val resultadoProceso = preResultadoProceso.asLiveData()

    fun recuperaListadoRecetasMockeableIO(peticion: PeticionServicioRecetas) = viewModelScope.launch{
        _loaderPantalla.update { true } // Que se muestre el ProgressBar :)
        val respuestaServicio = recetasRecuperarCasoUso(peticion)
        _loaderPantalla.update { false } // Que se oculte el ProgressBar :)
        when(respuestaServicio){
            is Result.Success -> preResultadoProceso.update { UIState.Success(respuestaServicio.data)}
            is Result.Error -> preResultadoProceso.update { UIState.Error(respuestaServicio.error)}
        }

        preResultadoProceso.update{ UIState.InitialState }

    }

}