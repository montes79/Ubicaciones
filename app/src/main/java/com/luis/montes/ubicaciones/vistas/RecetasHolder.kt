package com.luis.montes.ubicaciones.vistas

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.luis.montes.ubicaciones.R
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


        val objetoImagen=receta.imagenes.filter{it-> it.isPortada} //Seleccionando la imagen portada del listado
        val urlImagen= objetoImagen.let{
            it[0].UrlImagen
        }

       if(urlImagen!=null) { // urlImagen.let { }
           Glide.with(vistaAsociada.imagenReceta.context)
               .load(urlImagen)
               .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))//Que Glide decida o NONE para que no guarde cache
               .into<DrawableImageViewTarget>(DrawableImageViewTarget(vistaAsociada.imagenReceta))
       }
    }

}