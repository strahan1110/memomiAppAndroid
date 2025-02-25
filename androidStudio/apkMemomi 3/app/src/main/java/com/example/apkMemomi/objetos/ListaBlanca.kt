package com.example.apkMemomi.objetos

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ListaBlanca(
    @SerializedName ("idLista") val IdLista: String ?,
    @SerializedName("idEvento") val IdEvento: String,
    @SerializedName ("invitadoDni") val InvitadoDNI: String,
    @SerializedName("fechaEvento") val FechaEvento: String,
    @SerializedName ("codigo") var Codigo: String = ""
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
