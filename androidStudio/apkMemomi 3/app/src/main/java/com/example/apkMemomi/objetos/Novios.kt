package com.example.apkMemomi.objetos

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Novios(
    @SerializedName("idNovio")var IdNovio: String?,
    @SerializedName("nombreNovio1") var NombreNovio1: String,
    @SerializedName("nombreNovio2") var NombreNovio2: String,
    @SerializedName("correo") var Correo: String,
    @SerializedName("contrasena") var Contraseña: String,
    @SerializedName("telefono") var Telefono: String
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}

data class ApiRptaNovio(
    val isOk: Boolean,
    val message: String,
    val id: String,
    val nombre: String
)


data class user(
    @SerializedName("correo") var Correo2: String,
    @SerializedName("contrasena") var Contraseña2: String,
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}


