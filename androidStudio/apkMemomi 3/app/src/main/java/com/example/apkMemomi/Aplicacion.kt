package com.example.apkMemomi

import android.app.Application
import android.content.Context
import com.example.apkMemomi.objetos.*

class Aplicacion: Application() {

    companion object {
        lateinit var appContext: Context
        var resultado = ""
        var eventos = ArrayList<Eventos>()
        var novios = ArrayList<Novios>()
        var audios = ArrayList<Audios>()
        var fotos = ArrayList<Fotos>()
        var listablanca = ArrayList<ListaBlanca>()
        var comentarios = ArrayList<Comentarios>()
        var planusuario = ArrayList<PlanUsuario>()
        var videos = ArrayList<Videos>()


        val PERMISSION_REQUEST_CODE = 456

        fun initialize() {
            resultado = "NO HAY DATOS GUARDADOS"
            eventos = ArrayList()
            novios = ArrayList()
            audios = ArrayList()
            fotos = ArrayList()
            listablanca = ArrayList()
            comentarios = ArrayList()
            planusuario = ArrayList()
            videos = ArrayList()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initialize()
    }
}
