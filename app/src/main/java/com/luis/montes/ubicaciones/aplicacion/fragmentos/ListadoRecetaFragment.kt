package com.luis.montes.ubicaciones.aplicacion.fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.luis.montes.ubicaciones.R
import com.luis.montes.ubicaciones.aplicacion.RecetasDroidApp
import com.luis.montes.ubicaciones.aplicacion.repositorio.RecetasRepositorio
import com.luis.montes.ubicaciones.casosdeuso.RecetasRecuperarCasoUso
import com.luis.montes.ubicaciones.data.modelos.Recetas
import com.luis.montes.ubicaciones.data.modelos.peticion.PeticionServicioRecetas
import com.luis.montes.ubicaciones.data.viewmodels.RecetasViewModel
import com.luis.montes.ubicaciones.data.viewmodels.factory.RecetasVMFactory
import com.luis.montes.ubicaciones.databinding.FragmentListadoRecetaBinding
import com.luis.montes.ubicaciones.utilidades.UIState
import com.luis.montes.ubicaciones.vistas.RecetasAdaptador
import androidx.navigation.fragment.findNavController

class ListadoRecetaFragment : Fragment() {

    private lateinit var binding: FragmentListadoRecetaBinding

    private val retrofitInstancia = RecetasDroidApp.getInstanciaInterfazRetrofit()

    private lateinit var adaptadorRV: RecetasAdaptador
    //Por default es el usuario Luis_Montes
    private lateinit var peticionServicioRecetas: PeticionServicioRecetas

    private val vmRecetas: RecetasViewModel by viewModels {
        RecetasVMFactory(RecetasRecuperarCasoUso(RecetasRepositorio(retrofitInstancia)))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)

        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_listado_receta, container, false)
        inicializarFragmento()
        return binding.root
    }


    private fun inicializarFragmento(){
        //return inflater.inflate(R.layout.fragment_listado_receta, container, false)
        RecetasDroidApp.crearCanalNotificacion()
        peticionServicioRecetas = PeticionServicioRecetas()
        preparaRecuperacionListado()
        agregaListeners()
        inicializarAdaptador()
        // findNavController(R.id.navHostInicial).navigate(R.id.detalleRecetaFragment)
        inicializarAdaptador()
        llamarRecuperacionListado()
        //findNavController().navigate(R.id.navHostInicial)
    }

    private fun agregaListeners() {
        /*
        binding.btnLlamada.setOnClickListener {
             findNavController(R.id.navHostInicial).navigate(R.id.detalleRecetaFragment)
        }

         */
    }

    private fun inicializarAdaptador() {
        adaptadorRV = RecetasAdaptador(
            arrayListOf()
        ) { elemento -> listenerTapRecyclerView(elemento) }

        binding.rvRecetas.adapter = adaptadorRV

    }


    private fun listenerTapRecyclerView(receta: Recetas) {
        Toast.makeText(requireContext(), receta.nombre, Toast.LENGTH_SHORT).show()
    }



    private fun llamarRecuperacionListado() {
        binding.barraProgreso.visibility = View.VISIBLE
        vmRecetas.recuperaListadoRecetasMockeableIO(peticionServicioRecetas)
    }



    private fun preparaRecuperacionListado() {  // viewLifecycleOwner -> Fragment
        vmRecetas.resultadoProceso.observe(this) { estado ->
            when (estado) {
                is UIState.Success -> {
                    Log.d("RECETAS", "Llamada ok")
                    binding.barraProgreso.visibility = View.GONE
                    actualizaRecyclerRecetas(estado.data.datosRecetas)
                }
                is UIState.Error -> {
                    binding.barraProgreso.visibility = View.GONE
                    // Ocultar cuadro de dialogo de error y/o reintentar
                    Log.d(
                        "RECETAS",
                        "Ocurrio un error:* ${estado.error}, la excepcion es: ${estado.exception} *"
                    )
                }
                else -> Unit
            }

        }

    }




    @SuppressLint("NotifyDataSetChanged")
    private fun actualizaRecyclerRecetas(datosRecetas: List<Recetas>) {
        adaptadorRV.listaRecetas = datosRecetas.toMutableList()
        adaptadorRV.notifyDataSetChanged()
    }





}