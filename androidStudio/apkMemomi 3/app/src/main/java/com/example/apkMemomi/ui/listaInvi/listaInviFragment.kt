package com.example.apkMemomi.ui.listaInvi

import java.security.MessageDigest
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apkMemomi.adapter.ListaBAdapter
import com.example.apkMemomi.objetos.ListaBlanca
import com.example.apkMemomi.databinding.CreacioninviBinding
import com.example.apkMemomi.objetos.RetrofitClient
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class listaInviFragment : Fragment() {

    private var _binding: CreacioninviBinding? = null
    private val binding get() = _binding!!
    private val invitadosList = mutableListOf<ListaBlanca>()
    private lateinit var adapter: ListaBAdapter
    private var qrGenerado = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreacioninviBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configuración del RecyclerView para la lista de invitados
        adapter = ListaBAdapter(invitadosList)
        binding.guestListRecycler.layoutManager = LinearLayoutManager(context)
        binding.guestListRecycler.adapter = adapter

        var contadorIdLista = 4  // Inicia desde 4, ya que el valor inicial es "L0004"

        binding.addGuestButton.setOnClickListener {
            if (!qrGenerado) {
                val dni = binding.guestDniInput.text.toString()
                if (dni.isNotEmpty()) {
                    // Genera el nuevo IdLista incrementado
                    val nuevoIdLista = "L" + contadorIdLista.toString().padStart(4, '0')

                    // Crea un nuevo invitado con el IdLista generado
                    val nuevoInvitado = ListaBlanca(
                        IdEvento = "E0003",
                        IdLista = nuevoIdLista,
                        InvitadoDNI = dni,
                        FechaEvento = obtenerFechaEvento(),
                        Codigo = "" // Esto puede permanecer vacío por ahora
                    )

                    // Incrementa el contador para el próximo invitado
                    contadorIdLista++

                    // Agrega el nuevo invitado a la lista y actualiza el adaptador
                    invitadosList.add(nuevoInvitado)
                    adapter.notifyDataSetChanged()
                    binding.guestDniInput.text.clear()
                } else {
                    Toast.makeText(context, "Por favor ingrese un DNI", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "El QR ya fue generado. No se pueden agregar más DNIs", Toast.LENGTH_SHORT).show()
            }
        }



        /*
        binding.addGuestButton.setOnClickListener {
            if (!qrGenerado) {
                val dni = binding.guestDniInput.text.toString()
                //val lista = null;
                if (dni.isNotEmpty()) {
                    val nuevoInvitado = ListaBlanca(
                        IdEvento = "E0003",
                        IdLista = "L0004",
                        InvitadoDNI = dni,
                        FechaEvento = obtenerFechaEvento(),
                        Codigo = "" // Esto puede permanecer vacío por ahora
                    )
                    invitadosList.add(nuevoInvitado)
                    adapter.notifyDataSetChanged()
                    binding.guestDniInput.text.clear()
                } else {
                    Toast.makeText(context, "Por favor ingrese un DNI", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "El QR ya fue generado. No se pueden agregar más DNIs", Toast.LENGTH_SHORT).show()
            }
        }*/

        // Dentro de tu función para generar el código QR
        binding.generateQRCodeButton.setOnClickListener {
            if (!qrGenerado && invitadosList.isNotEmpty()) {
                // Concatenar los DNIs
                val dniConcatenados = invitadosList.joinToString(",") { it.InvitadoDNI }

                // Generar un hash único para representar la lista de DNIs
                val hash = generarHash(dniConcatenados)

                // Generar el código QR usando el hash
                val codigoQR = generateQRCode(hash)

                if (codigoQR != null) {
                    // Asignar el hash (o código generado) como el valor para todos los invitados
                    invitadosList.forEach { it.Codigo = hash }

                    // Mostrar el código QR en la UI
                    binding.qrCodeImageView.setImageBitmap(codigoQR)
                    qrGenerado = true

                    // Guardar todos los invitados con el código generado en la base de datos
                    agregarTodosLosInvitados() // Llamada para guardar en la base de datos

                    Toast.makeText(context, "Código QR generado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error al generar el código QR", Toast.LENGTH_SHORT).show()
                }
            } else if (qrGenerado) {
                Toast.makeText(context, "El QR ya ha sido generado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No hay DNIs en la lista para generar QR", Toast.LENGTH_SHORT).show()
            }
        }

        binding.resetButton.setOnClickListener {
            invitadosList.clear()
            qrGenerado = false
            binding.qrCodeImageView.setImageBitmap(null)
            adapter.notifyDataSetChanged()
            Toast.makeText(context, "Lista de DNIs reiniciada. Puede agregar nuevos DNIs", Toast.LENGTH_SHORT).show()
        }

        return root
    }
    /*
    fun generarHash(dnis: String): String {
        val md = MessageDigest.getInstance("MD5")
        val hashBytes = md.digest(dnis.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) } // Convierte los bytes a hexadecimal
    }*/

    fun generarHash(dnis: String): String {
        val md = MessageDigest.getInstance("MD5")
        val hashBytes = md.digest(dnis.toByteArray())
        val hashCompleto = hashBytes.joinToString("") { "%02x".format(it) } // Convierte a hexadecimal
        return hashCompleto.substring(0, 8) // Toma solo los primeros 8 caracteres
    }


    private fun obtenerFechaEvento(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return format.format(calendar.time)
    }

    private fun generateQRCode(data: String): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun agregarTodosLosInvitados() {
        invitadosList.forEach { invitado ->
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    // Log para verificar datos antes de enviarlos
                    println("Enviando invitado a la API: ${invitado.InvitadoDNI}")
                    val response = RetrofitClient.api.agregarListaBlanca(invitado)

                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Invitado ${invitado.InvitadoDNI} agregado correctamente", Toast.LENGTH_SHORT).show()
                            println("Invitado agregado correctamente en la base de datos: ${invitado.InvitadoDNI}")
                        }
                    } else {
                        // Mostrar el error si la respuesta no es exitosa
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        withContext(Dispatchers.Main) {
                            showError("Error al agregar invitado ${invitado.InvitadoDNI}: $errorMessage")
                            println("Error en respuesta de API: $errorMessage")
                        }
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        showError("Error de conexión: ${e.message}")
                        println("Error de conexión: ${e.message}")
                    }
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        showError("Error HTTP: ${e.message}")
                        println("Error HTTP: ${e.message}")
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



