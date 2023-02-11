package com.luis.montes.ubicaciones.data.modelos

import com.google.gson.annotations.SerializedName

data class Recetas(
    @SerializedName("idReceta") val id:Int,
    @SerializedName("nombreReceta") val nombre:String,
    @SerializedName("fechaPublicacion") val fecha:String,
    @SerializedName("modoPreparacion") val instrucciones:List<String> = arrayListOf(),
    @SerializedName("pais") val pais:String,
    @SerializedName("latitud") val latitud:Double,
    @SerializedName("longitud") val longitud:Double,
    @SerializedName("ingredientes") val ingredientes:List<Ingredientes> = arrayListOf(),
    @SerializedName("imagenes") val imagenes:List<Imagenes> = arrayListOf()
)
