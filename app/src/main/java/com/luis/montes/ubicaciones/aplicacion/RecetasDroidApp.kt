package com.luis.montes.ubicaciones.aplicacion

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.luis.montes.ubicaciones.BuildConfig
import com.luis.montes.ubicaciones.servicios.RecetasServicio
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

class RecetasDroidApp : Application() {


    override fun onCreate() {
        super.onCreate()
    }

    init {
        instance = this
    }




    companion object {

        lateinit var instance: RecetasDroidApp
            private set

        fun contextoAplicacion(): Context {
            return instance.applicationContext
        }


        private val sslContext: SSLContext = SSLContext.getInstance("SSL") //SSL TLS

        //const
        val TIME_OUT_APP: Long = 5 // Minutos

        private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
           // @Throws(CertificateException::class)
           @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            //@Throws(CertificateException::class)
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })



        fun getInstanciaInterfazRetrofit(): RecetasServicio {

            val retrofitInstancia = Retrofit.Builder()
                .client(clientOkHttp())
                .baseUrl(BuildConfig.url_api)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .build()
            return retrofitInstancia.create(RecetasServicio::class.java)
        }


        private fun clientOkHttp(retry: Boolean = true): OkHttpClient {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor { message ->
                    Platform.get().log(message)
                    Log.d("httpLogInterceptor", message)
                }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val builder = OkHttpClient.Builder()
            //Vamos a visualizar las llamadas http
            val colectorChucker =ChuckerCollector(contextoAplicacion(),true,RetentionManager.Period.ONE_HOUR)
            val interceptorChuker = ChuckerInterceptor(contextoAplicacion(), colectorChucker,250_000L)
            builder.addInterceptor(interceptorChuker)
            builder.addInterceptor(ChuckerInterceptor(contextoAplicacion()))



            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            builder.connectTimeout(TIME_OUT_APP, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_APP, TimeUnit.MINUTES)
                .addInterceptor(httpLoggingInterceptor)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .retryOnConnectionFailure(retry)
            builder.hostnameVerifier { _, _ -> true }
           // builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            return builder.build()
        }


        val CHANNEL_ID="IDYAPENOTIF"
        fun crearCanalNotificacion() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //val name = getString(R.string.channel_name)
                val nombre= "YapeNotificaciones"
                //val descriptionText = getString(R.string.channel_description)
                val textoDescriptivo = "Descripcion de la YAPE Notificacion"
                val importancia = NotificationManager.IMPORTANCE_DEFAULT
                val canalNotificacion = NotificationChannel(CHANNEL_ID, nombre, importancia).apply {
                    description = textoDescriptivo
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    contextoAplicacion().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(canalNotificacion)
            }
        }

    }

}