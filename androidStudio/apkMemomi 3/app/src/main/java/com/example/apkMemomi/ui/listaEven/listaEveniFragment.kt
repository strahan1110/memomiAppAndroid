package com.example.apkMemomi.ui.listaEven

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apkMemomi.Aplicacion
import com.example.apkMemomi.R
import com.example.apkMemomi.adapter.EventoAdapter
import com.example.apkMemomi.databinding.ListaevenBinding
import com.example.apkMemomi.objetos.Eventos
import com.example.apkMemomi.objetos.RetrofitClient
import com.example.apkMemomi.objetos.SharedPrefManager
import com.example.apkMemomi.ui.detallevento.detalleEFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class listaEveniFragment : Fragment() {

    private var _binding: ListaevenBinding? = null
    private val binding get() = _binding!!
    private lateinit var eadapter: EventoAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListaevenBinding.inflate(inflater, container, false)

        eadapter = EventoAdapter(Aplicacion.eventos) { evento ->
            val detalleFragment = detalleEFragment().apply {
                arguments = Bundle().apply {
                    putString("IdEvento", evento.IdEvento)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detalleFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewEventos.adapter = eadapter
        binding.recyclerViewEventos.layoutManager = LinearLayoutManager(context) // Mueve el LayoutManager aquí

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtenerEventos() // Cargar eventos al inicializar
    }

    private fun obtenerEventos() {

        val valor = SharedPrefManager.obtener(requireContext(),"idUsuario","sin valor").toString()
        Toast.makeText(context,"valor $valor",Toast.LENGTH_SHORT).show()

        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                // Pasar el id como parámetro de consulta usando @Query
                RetrofitClient.api.getEventoById(valor)
            } catch (e: IOException) {
                // Error de red o conexión
                withContext(Dispatchers.Main) {
                    showAlert("Error de conexión: ${e.message}")
                }
                return@launch
            } catch (e: HttpException) {
                // Error del servidor (4XX, 5XX)
                withContext(Dispatchers.Main) {
                    showAlert("Error HTTP: ${e.code()} - ${e.message()}")
                }
                return@launch
            } catch (e: Exception) {
                // Otro tipo de error
                withContext(Dispatchers.Main) {
                    showAlert("Error inesperado: ${e.message}")
                }
                return@launch
            }


            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val eventosRecibidos = response.body() ?: emptyList()

                    // Limpia y actualiza la lista de eventos
                    Aplicacion.eventos.clear()
                    Aplicacion.eventos.addAll(eventosRecibidos)
                    eadapter.notifyDataSetChanged()
                }
            } else {
                withContext(Dispatchers.Main) {
                    showAlert("Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }
        }
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            Log.e("API_ERROR", message)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAlert(mensaje: String) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Alerta")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss() // Cierra el diálogo
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss() // También cierra el diálogo
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


}
