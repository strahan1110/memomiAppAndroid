package com.example.apkMemomi.ui.registronovios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.apkMemomi.R
import com.example.apkMemomi.databinding.RegistronoviosBinding
import com.example.apkMemomi.objetos.RetrofitClient
import com.example.apkMemomi.objetos.Novios
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class registronoviosFragment : Fragment() {
    private var _bindingN: RegistronoviosBinding? = null
    private val binding get() = _bindingN!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingN = RegistronoviosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.botonatras.setOnClickListener {
            findNavController().navigate(R.id.devueltalogin)
        }

        binding.btnRegister.setOnClickListener {
            val nombreNovio = binding.etNombre1.text.toString()
            val nombreNovia = binding.etNombre2.text.toString()
            val correo = binding.etCorreo.text.toString()
            val telefono = binding.etTelefono.text.toString()
            val contrasena = binding.etPassword.text.toString()
            val confirmarContrasena = binding.etConfirmarPassword.text.toString()

            if (contrasena == confirmarContrasena) {
                registrarnovios(nombreNovio, nombreNovia, correo, telefono, contrasena)
            } else {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun registrarnovios(nov1: String, nov2: String, correo: String, telef: String, psd: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val nuevoNovio = Novios(
                IdNovio = "", // Este valor debe ser vacio
                NombreNovio1 = nov1,
                NombreNovio2 = nov2,
                Correo = correo,
                Telefono = telef,
                Contraseña = psd
            )

            try {
                val response = RetrofitClient.api.registrarNovios(nuevoNovio)

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.devueltalogin)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error HTTP: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                // Error de conexión
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                // Error HTTP
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingN = null
    }
}
