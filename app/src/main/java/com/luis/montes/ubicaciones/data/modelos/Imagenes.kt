package com.luis.montes.ubicaciones.data.modelos

import com.google.gson.annotations.SerializedName

data class Imagenes(
    @SerializedName("idImagen") val idImagen:Int,
    @SerializedName("urlImagen") val UrlImagen:String="#",
    @SerializedName("isPortada") val isPortada:Boolean=false,
    @SerializedName("orden") val orden:Int=0
)
