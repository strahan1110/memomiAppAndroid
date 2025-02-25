package com.example.apkMemomi.objetos

import com.example.apkMemomi.ui.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory

object RetrofitClient {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(apiClients.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())  // corrutinas no se como es..pero recomendan por generar listas
            .build()
            .create(ApiService::class.java)
    }
}

