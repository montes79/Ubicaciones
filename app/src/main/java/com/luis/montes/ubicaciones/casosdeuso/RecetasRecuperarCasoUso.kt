package com.luis.montes.ubicaciones.casosdeuso

import android.util.Log
import com.luis.montes.ubicaciones.aplicacion.repositorio.RecetasRepositorio
import com.luis.montes.ubicaciones.data.modelos.respuesta.RespuestaServicioRecetas
import com.luis.montes.ubicaciones.data.modelos.peticion.PeticionServicioRecetas
import com.luis.montes.ubicaciones.utilidades.Result

class RecetasRecuperarCasoUso (private val repositorio: RecetasRepositorio) {

    suspend operator fun invoke(peticion: PeticionServicioRecetas): Result<RespuestaServicioRecetas>{
        try{
            val respuesta = repositorio.recuperaListadoRecetas(peticion)
            val bandera=respuesta.isSuccessful
            val banderaNull = respuesta.body() == null
            if(!bandera || banderaNull) {
                Log.d("RECETAS","Es exitoso: $bandera, esNulo el Body: $banderaNull")
                return Result.Error("No fue exitoso o el body es null","Excepcion generica")
            }

            val contenido = respuesta.body()!!
            if(contenido.codigo!=200) {
                Log.d("RECETAS","Codigo de Respuesta: ${contenido.codigo}")
                return Result.Error("Codigo no es 200","Excepcion generica")
            }

            return Result.Success(contenido)

        }catch (e:Exception){
            return Result.Error("Error o excepcion",e.message.toString())
        }

    }

}