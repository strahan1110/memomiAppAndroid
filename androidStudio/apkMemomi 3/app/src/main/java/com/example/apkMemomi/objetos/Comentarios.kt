package com.example.apkMemomi.objetos

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Comentarios(
    @SerializedName ("idComentario") val IdComentario: String = "",
    @SerializedName ("idEvento") val IdEvento: String = "",
    @SerializedName ("comentario") val Comentario: String = "",
    @SerializedName ("invitadoDNI") val InvitadoDNI: String = "",
    @SerializedName ("nombreInvitado") val NombreInvitado: String = ""
)  {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
