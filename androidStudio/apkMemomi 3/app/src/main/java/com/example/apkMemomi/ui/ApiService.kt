package com.example.apkMemomi.ui

import android.provider.MediaStore.Audio
import com.example.apkMemomi.objetos.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("Eventoes")
    suspend fun getEventos(): Response<List<Eventos>>

    @POST("Eventoes")
    suspend fun agregarEvento(@Body evento: Eventos): Response<Eventos>

    @GET("Eventoes/EventoxNovio")  // Usa {id} en la ruta
    suspend fun getEventoById(@Query("id") id: String): Response<List<Eventos>>

    @GET("Novios/{id}")
    suspend fun getNoviosById(@Path("id") id: String): Response<Novios>

    @GET("Novios")
    suspend fun obtenerNovios(): Response<List<Novios>>

    @POST ("Novios/LoginApp")
    suspend fun obtenerIdNovio(@Body usuario: user): Response<ApiRptaNovio>

    @POST("Novios")
    suspend fun registrarNovios(@Body novios: Novios): Response<Novios>





    @POST("Comentarios/PostComentarioxInvitado")
    suspend fun enviarMensaje(@Body coment : Comentarios): Response<ResponseBody>







    @GET("Fotoes")
    suspend fun getFotos(): Response<List<Fotos>>

    @GET("Listablancas")
    suspend fun obtenerListaInvitados(): Response<List<ListaBlanca>>

    @POST("Listablancas")
    suspend fun agregarListaBlanca(@Body listaBlanca: ListaBlanca): Response<ListaBlanca>

    @POST("Fotoes/PostFotoxInvitado")
    suspend fun postFotoesArchivo(@Body fotos: List<Fotos>): Response<ResponseBody>

    @POST("Videos/PostVideosxInvitado")
    suspend fun postVideo(@Body videos: List<Videos>) : Response<ResponseBody>

    @GET("Videos")
    suspend fun getVideos() : Response<List<Videos>>

    @POST("Audios/PostAudioxInvitado")
    suspend fun postAudio(@Body audios: List<Audios>): Response<ResponseBody>

    @GET("Audios")
    suspend fun obtenerAudios(): Response<List<Audios>>
}

