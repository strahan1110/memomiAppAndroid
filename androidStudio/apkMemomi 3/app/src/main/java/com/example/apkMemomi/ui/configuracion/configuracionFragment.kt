package com.example.apkMemomi.ui.configuracion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apkMemomi.databinding.ItemNovioBinding
import com.example.apkMemomi.databinding.RegistroevenBinding
import com.example.apkMemomi.objetos.Novios
import com.example.apkMemomi.objetos.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class configuracionFragment : Fragment() {

    private var _bindingC: ItemNovioBinding? = null
    private val binding get() = _bindingC!!

    private var idNovioSesion: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingC = ItemNovioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //traer nombres de lista de novios
        idNovioSesion = arguments?.getString("idnovios")

        idNovioSesion?.let { id ->
            obtenerNovioPorId(id)
        }
        return root
    }

    private fun obtenerNovioPorId(id: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response: Response<Novios> = RetrofitClient.api.getNoviosById(id)
                if (response.isSuccessful) {
                    val novio = response.body()
                    novio?.let {
                        mostrarDatosNovio(it)
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Manejo de errores, por ejemplo, mostrar mensaje de error al usuario
            }
        }
    }

    private fun mostrarDatosNovio(novio: Novios) {
        binding.etNombreNovio1.setText(novio.NombreNovio1)
        binding.etNombreNovio2.setText(novio.NombreNovio2)
        binding.etCorreo.setText(novio.Correo)
        binding.etTelefono.setText(novio.Telefono)

        // Si también deseas mostrar más detalles, como el de la novia:
        // binding.noviaNombre.text = novio.NombreNovia (si fuera necesario, dependiendo de los datos)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bindingC = null
    }
}

