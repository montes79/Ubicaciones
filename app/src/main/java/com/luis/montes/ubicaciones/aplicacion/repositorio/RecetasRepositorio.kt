package com.luis.montes.ubicaciones.aplicacion.repositorio

import android.util.Log
import com.luis.montes.ubicaciones.data.modelos.respuesta.RespuestaServicioRecetas
import com.luis.montes.ubicaciones.data.modelos.peticion.PeticionServicioRecetas
import com.luis.montes.ubicaciones.servicios.RecetasServicio
import retrofit2.Response

class RecetasRepositorio(private val servicio: RecetasServicio) {
    suspend fun recuperaListadoRecetas(peticion: PeticionServicioRecetas): Response<RespuestaServicioRecetas>{
           return servicio.getListadoRecetas(/*peticion*/)
    }
}