package com.example.apkMemomi.objetos

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PlanUsuario(
    @SerializedName("idPlan") var idPlan: Plan = Plan.P0002,  // Usamos el enum Plan
    @SerializedName("idNovio") var idNovio: String = "",
    @SerializedName("costos") var costos: Int = 0,
    @SerializedName("foto") var foto: Int = 0,
    @SerializedName("video") var video: Int = 0,
    @SerializedName("audio") var audio: Int = 0,
    @SerializedName("comentario") var comentario: Int = 0
) {
    // Convert the object to JSON string
    fun toJson(): String {
        return Gson().toJson(this)
    }
}

enum class Plan {
    P0001,
    P0002,
    P0003
}
