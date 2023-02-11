package com.luis.montes.ubicaciones.data.modelos

import com.google.gson.annotations.SerializedName

data class RespuestaServicioRecetas(
    @SerializedName("codigo") val codigo:Int,
    @SerializedName("mensaje") val mensaje:String,
    @SerializedName("datos") val datosRecetas:List<Recetas> = arrayListOf()
)
