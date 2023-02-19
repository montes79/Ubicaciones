package com.luis.montes.ubicaciones.vistas

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.luis.montes.ubicaciones.data.modelos.Recetas
import com.luis.montes.ubicaciones.databinding.ElementoRecetaBinding


class RecetasHolder(
    private val vistaAsociada:ElementoRecetaBinding,
    val listener:(receta: Recetas)->Unit
): RecyclerView.ViewHolder(vistaAsociada.root) {

    fun asociarElementoHolder(receta: Recetas){
        vistaAsociada.nombrePais.text=receta.pais
        vistaAsociada.nombrePlatillo.text=receta.nombre
        itemView.setOnClickListener {
            listener(receta)
        }

        val imagen= receta.imagenes[0].UrlImagen

        //Glide.with(vistaAsociada.imagenReceta.context)
         //   .load(receta.imagenes[0])
         //   .into(vistaAsociada.imagenReceta)



        Glide.with(vistaAsociada.imagenReceta.context)
            .load(imagen)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into<DrawableImageViewTarget>(DrawableImageViewTarget(vistaAsociada.imagenReceta))


    }

}