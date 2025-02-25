package com.example.apkMemomi.objetos

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Fotos(
    @SerializedName ("idFotos") val IdFotos: String = "",
    @SerializedName ("idEvento") val IdEvento: String = "",
    @SerializedName ("fotos") val Fotos: String = "",
    @SerializedName ("fotoFisic") val FotoFisic: String = "",
    @SerializedName ("invitadoDNI") val InvitadoDNI: String = "",
    @SerializedName ("nombreInvitado") val NombreInvitado: String = "",
    @SerializedName ("fotoBS64") val FotoBS64: String = ""
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}

