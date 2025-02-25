package com.example.apkMemomi.objetos

import android.media.MediaPlayer
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Videos(
    @SerializedName ("idVideo") val IdVideo: String ?,
    @SerializedName ("idEvento") val IdEvento: String,
    @SerializedName ("videos") val Videos: String = "",
    @SerializedName ("videoFisic") val VideoFisic: String = "",
    @SerializedName ("invitadoDNI") val InvitadoDNI: String,
    @SerializedName ("nombreInvitado") val NombreInvitado: String = "",
    @SerializedName ("videoBS64") val VideoBS64: String = ""

)  {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
