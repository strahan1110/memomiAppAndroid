package com.example.apkMemomi.objetos

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Audios(
    @SerializedName("idAudio") val IdAudio: String?,
    @SerializedName("idEvento")  val IdEvento: String = "",
    @SerializedName("audios")  val Audio: String = "",
    @SerializedName("audioFisic")  val AudioFisc: String = "",
    @SerializedName("invitadoDni")   val InvitadoDNI: String = "",
    @SerializedName("nombreInvitado")  val NombreInvitado: String = "",
    @SerializedName("audioBS64") val AudioBS64: String=""
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
