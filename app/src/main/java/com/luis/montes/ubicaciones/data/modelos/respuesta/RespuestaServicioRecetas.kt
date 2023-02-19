package com.luis.montes.ubicaciones.data.modelos.respuesta

import com.google.gson.annotations.SerializedName
import com.luis.montes.ubicaciones.data.modelos.Recetas

data class RespuestaServicioRecetas(
    @SerializedName("codigo") val codigo:Int,
    @SerializedName("mensaje") val mensaje:String,
    @SerializedName("datos") val datosRecetas:List<Recetas> = arrayListOf()
)
