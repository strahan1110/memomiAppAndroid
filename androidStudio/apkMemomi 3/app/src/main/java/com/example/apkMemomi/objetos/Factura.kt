package com.example.apkMemomi.objetos

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Factura(
    @SerializedName("idFactura") val IdFactura: String?,
    @SerializedName("fec_venc") val Fec_Venc: String,
    @SerializedName("fec_compra") val Fec_Compra: String,
    @SerializedName("tipo_pago") val Tipo_pago: String = "",
    @SerializedName("idPlan") val IdPlan: String = ""
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
