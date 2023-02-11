package com.luis.montes.ubicaciones.aplicacion

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.luis.montes.ubicaciones.BuildConfig
import com.luis.montes.ubicaciones.servicios.RecetasServicio
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

class RecetasDroidApp : Application() {

    private var instance: RecetasDroidApp = this
    private val sslContext: SSLContext = SSLContext.getInstance("SSL")
    //const
    val TIME_OUT_APP: Long = 10

    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            chain: Array<java.security.cert.X509Certificate>,
            authType: String
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            chain: Array<java.security.cert.X509Certificate>,
            authType: String
        ) {
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
        }
    })


    fun contextoAplicacion(): Context {
        return instance.applicationContext
    }


    fun getInstanciaRetrofit(): RecetasServicio {

        val retrofitInstancia= Retrofit.Builder()
            .client(clientOkHttp())
           .baseUrl(BuildConfig.url_api)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        return builder.build()
    }


}