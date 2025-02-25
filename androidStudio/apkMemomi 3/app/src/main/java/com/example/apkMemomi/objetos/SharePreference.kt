package com.example.apkMemomi.objetos

import android.content.Context

object SharedPrefManager {
    private const val PREF_NAME = "MiPreferencia"

    fun guardar(context: Context, clave: String, valor: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(clave, valor)
        editor.apply()
    }

    fun obtener(context: Context, clave: String, valorPorDefecto: String): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(clave, valorPorDefecto)
    }

    fun actualizar(context: Context, clave: String, nuevoValor: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(clave, nuevoValor) // Sobrescribe el valor existente
        editor.apply()
    }
}