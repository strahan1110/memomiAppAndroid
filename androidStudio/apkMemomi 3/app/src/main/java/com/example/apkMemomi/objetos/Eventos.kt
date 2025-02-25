package com.example.apkMemomi.objetos
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Eventos(
    @SerializedName("idEvento") val IdEvento: String?,
    @SerializedName("nombreEvento") val NombreEvento: String?,
    @SerializedName("fechaEvento") val FechaEvento: String?,
    @SerializedName("idNovio") val idNovio: String? = ""
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
