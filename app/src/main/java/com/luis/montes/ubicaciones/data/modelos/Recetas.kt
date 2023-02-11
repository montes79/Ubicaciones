package com.luis.montes.ubicaciones.data.modelos

import com.google.gson.annotations.SerializedName

data class Recetas(
    @SerializedName("idReceta") val id:Int,
    @SerializedName("nombreReceta") val nombre:String,
    @SerializedName("fechaPublicacion") val fecha:String,
    @SerializedName("ingredientes") val ingredientes:String,
)
