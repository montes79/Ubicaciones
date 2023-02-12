package com.luis.montes.ubicaciones.aplicacion.repositorio

import android.util.Log
import com.luis.montes.ubicaciones.data.modelos.RespuestaServicioRecetas
import com.luis.montes.ubicaciones.data.modelos.peticion.PeticionServicioRecetas
import com.luis.montes.ubicaciones.servicios.RecetasServicio
import retrofit2.Response

class RecetasRepositorio(private val servicio: RecetasServicio) {

    suspend fun recuperaListadoRecetas(peticion: PeticionServicioRecetas): Response<RespuestaServicioRecetas>{
        Log.d("RECETAS","RecetasRepositorio.recuperaListadoRecetas")
        return servicio.getListadoRecetas(/*peticion*/)
    }
}