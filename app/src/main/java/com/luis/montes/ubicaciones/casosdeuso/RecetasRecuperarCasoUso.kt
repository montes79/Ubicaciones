package com.luis.montes.ubicaciones.casosdeuso

import android.util.Log
import com.luis.montes.ubicaciones.aplicacion.repositorio.RecetasRepositorio
import com.luis.montes.ubicaciones.data.modelos.RespuestaServicioRecetas
import com.luis.montes.ubicaciones.data.modelos.peticion.PeticionServicioRecetas
import com.luis.montes.ubicaciones.utilidades.Result

class RecetasRecuperarCasoUso (private val repositorio: RecetasRepositorio) {

    suspend operator fun invoke(peticion: PeticionServicioRecetas): Result<RespuestaServicioRecetas>{
        try{
            Log.d("RECETAS","INVOKE")
            val respuesta = repositorio.recuperaListadoRecetas(peticion)
            if(!respuesta.isSuccessful || respuesta.body() == null) {
                Log.d("RECETAS","INVOKE SIN BODY o NO SUCCESS")
                return Result.Error()
            }

            val contenido = respuesta.body()!!
            if(contenido.codigo!=200) {
                Log.d("RECETAS","INVOKE CODIGO!=200")
                return Result.Error()
            }

            Log.d("RECETAS","contenido: {${contenido.datosRecetas}}")
            return Result.Success(contenido)

        }catch (e:Exception){
            return Result.Error()
        }

    }

}