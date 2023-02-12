package com.luis.montes.ubicaciones.data.modelos.peticion

import com.google.gson.annotations.SerializedName

data class PeticionServicioRecetas(
    @SerializedName("usuario") val usuario : String ="Luis_Montes"
)
