package com.luis.montes.ubicaciones.data.modelos

import com.google.gson.annotations.SerializedName

data class Ingredientes(
    @SerializedName("idIngrediente") val idIngrediente:Int,
    @SerializedName("nombre") val nombre:String,
    @SerializedName("cantidad") val cantidad:Int,
    @SerializedName("unidadMedida") val unidadMedida:String,
)
