package com.luis.montes.ubicaciones

import android.Manifest
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.luis.montes.ubicaciones.aplicacion.RecetasDroidApp
import com.luis.montes.ubicaciones.aplicacion.repositorio.RecetasRepositorio
import com.luis.montes.ubicaciones.casosdeuso.RecetasRecuperarCasoUso
import com.luis.montes.ubicaciones.data.modelos.Recetas
import com.luis.montes.ubicaciones.data.modelos.peticion.PeticionServicioRecetas
import com.luis.montes.ubicaciones.data.viewmodels.RecetasViewModel
import com.luis.montes.ubicaciones.data.viewmodels.factory.RecetasVMFactory
import com.luis.montes.ubicaciones.databinding.ActivityMainBinding
import com.luis.montes.ubicaciones.utilidades.UIState

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val retrofitInstancia =RecetasDroidApp.getInstanciaInterfazRetrofit()

    private lateinit var  listaRecetas: ArrayList<Recetas>

    //Por default es el usuario Luis_Montes
    private lateinit var peticionServicioRecetas: PeticionServicioRecetas

    private val vmRecetas : RecetasViewModel by viewModels{
        RecetasVMFactory(RecetasRecuperarCasoUso(RecetasRepositorio(retrofitInstancia)))
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        RecetasDroidApp.crearCanalNotificacion()
        peticionServicioRecetas = PeticionServicioRecetas()
        enableNotifications()
        Log.d("RECETAS","onCreate preparaRecuperacionListado")
        preparaRecuperacionListado()
        Log.d("RECETAS","onCreate llamarRecuperacionListado")
        creaNotificacionBuilder()

        agregaListeners()
    }


    private fun agregaListeners(){
        binding.btnLlamada.setOnClickListener {
            llamarRecuperacionListado()
        }
    }


    private fun creaNotificacionBuilder(){
        /*
        val intent = Intent(this, AlertDetails::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        */

        val builder = NotificationCompat.Builder(this, RecetasDroidApp.CHANNEL_ID)
                 .setSmallIcon(com.google.android.material.R.drawable.notify_panel_notification_icon_bg)
                 .setContentTitle("My notification")
                 .setContentText("Hello World!")
                 .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                 // .setContentIntent(pendingIntent)
                 .setAutoCancel(true)


            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(100600, builder.build())
            }

    }

    private fun llamarRecuperacionListado(){
        vmRecetas.recuperaListadoRecetasMockeableIO(peticionServicioRecetas)
    }

    private fun preparaRecuperacionListado(){  // viewLifecycleOwner -> Fragment
        vmRecetas.resultadoProceso.observe(this){ estado ->
            when (estado){
                    is UIState.Success -> {
                        Log.d("RECETAS","Llamada ok")
                    }
                    is UIState.Error -> {
                        // Mostrar cuadro de dialogo de error y/o reintentar
                        Log.d("RECETAS","Ocurrio un error")
                    }
                else -> Unit
            }

        }

    }



    private fun enableNotifications() {
        if (!ensureNotificationsPermission()) return

    }

    private fun ensureNotificationsPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true

        val permission = Manifest.permission.POST_NOTIFICATIONS
        val granted = ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) return true

        if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(this).setTitle(R.string.app_name)
                .setMessage("Motivo del permiso").setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                 notificationsPermissionLauncher.launch(permission)
                }.setNegativeButton("Negativo") { _, _ ->

                }.show()

            return false
        }

        notificationsPermissionLauncher.launch(permission)

        return false
    }


    enum class PermissionType {
        NOTIFICATIONS,
        LOCATION
    }


    private val notificationsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
                  return@registerForActivityResult
        }

        if (shouldOpenSettings(PermissionType.NOTIFICATIONS)) {
            showSettingsPrompt(PermissionType.NOTIFICATIONS)
            return@registerForActivityResult
        }


    }


    private val openSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
            if (NotificationManagerCompat.from(this.applicationContext).areNotificationsEnabled()) {

            } else {

            }

    }

    private fun shouldOpenSettings(permissionType: PermissionType): Boolean {
        val permission = when (permissionType) {
            PermissionType.NOTIFICATIONS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.POST_NOTIFICATIONS
                } else {
                    null
                }
            }
            PermissionType.LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
        }

        if (permission != null) {
            if (!shouldShowRequestPermissionRationale(permission) && pendingRationales.contains(permissionType)) {
                pendingRationales.remove(permissionType)
                return false
            }

            if (shouldShowRequestPermissionRationale(permission) && pendingRationales.contains(permissionType)) {
                pendingRationales.remove(permissionType)
                return false
            }

            if (shouldShowRequestPermissionRationale(permission) && !pendingRationales.contains(permissionType)) {
                return false
            }
        }

        return true
    }


    private val pendingRationales = mutableListOf<PermissionType>()



    private fun showSettingsPrompt(permissionType: PermissionType) {
        AlertDialog.Builder(this).setTitle(R.string.app_name)
            .setMessage("Permiso rationales")
            .setCancelable(false)
            .setPositiveButton("PositivoM") { _, _ ->
                   openSettingsLauncher.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                })
            }
            .setNegativeButton("NegativoSSP") { _, _ ->
                    when (permissionType) {
                    PermissionType.NOTIFICATIONS -> {}
                    PermissionType.LOCATION -> {}
                }
            }
            .show()
    }
}