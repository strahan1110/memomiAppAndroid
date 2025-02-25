package com.example.apkMemomi.ui.detallevento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apkMemomi.databinding.DetalleventoBinding
import com.example.apkMemomi.objetos.Eventos
import com.example.apkMemomi.objetos.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class detalleEFragment : Fragment() {

    private var _binding: DetalleventoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetalleventoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventoId = arguments?.getString("idEvento") ?: return
        obtenerEventoPorId(eventoId)
    }

    private fun obtenerEventoPorId(eventoId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getEventoById(eventoId) // Llamada a la API para obtener el evento

                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        val evento = response.body()!!
                        //binding.tvNombreEvento.text = evento.NombreEvento
                        //binding.tvFechaEvento.text = evento.FechaEvento
                    }
                } else {
                    showError("Error al obtener los detalles del evento")
                }
            } catch (e: IOException) {
                showError("Error de conexión: ${e.message}")
            } catch (e: HttpException) {
                showError("Error HTTP: ${e.message}")
            }
        }
    }

    private fun mostrarDetallesEvento(evento: Eventos) {
        binding.tvNombreEvento.text = evento.NombreEvento
        binding.tvFechaEvento.text = evento.FechaEvento
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
