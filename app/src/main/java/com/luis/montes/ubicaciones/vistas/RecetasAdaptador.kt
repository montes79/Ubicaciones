package com.luis.montes.ubicaciones.vistas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luis.montes.ubicaciones.data.modelos.Recetas
import com.luis.montes.ubicaciones.databinding.ElementoRecetaBinding

/*
    var listaProcesosREF:MutableList<ListadoRefinanciamientosPorMasterContract>,
    var listener: (elemento: ListadoRefinanciamientosPorMasterContract)->Unit
*/
class RecetasAdaptador(
    var listaRecetas:MutableList<Recetas>,
    private val listener:(elemento:Recetas)->Unit
):   RecyclerView.Adapter<RecetasHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetasHolder {
        val vistaRoot=ElementoRecetaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecetasHolder(vistaRoot,listener)
    }

    override fun onBindViewHolder(holder: RecetasHolder, posicion: Int) {
          holder.asociarElementoHolder(listaRecetas[posicion])
    }

    override fun getItemCount()=listaRecetas.size
}