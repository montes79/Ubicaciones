package com.luis.montes.ubicaciones.servicios

import com.luis.montes.ubicaciones.data.modelos.RespuestaServicioRecetas
import com.luis.montes.ubicaciones.data.modelos.peticion.PeticionServicioRecetas
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import io.reactivex.Single
import retrofit2.Response

interface RecetasServicio {

    // http://demo1787648.mockable.io/listadoRecetas
    @Headers("Content-Type: application/json")
    @GET("/listadoRecetas") // Se agregara al url de peticion
    //@Query("usuario") peticion: PeticionServicioRecetas
    suspend fun getListadoRecetas() : Response<RespuestaServicioRecetas>
}