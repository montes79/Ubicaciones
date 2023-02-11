package com.luis.montes.ubicaciones.servicios

import com.luis.montes.ubicaciones.data.modelos.RespuestaServicioRecetas
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import io.reactivex.Single

interface RecetasServicio {

    // http://demo1787648.mockable.io/listadoRecetas
    @Headers("Content-Type: application/json")
    @GET("/listadoRecetas")
    fun getListadoRecetas() : Single<RespuestaServicioRecetas>
}