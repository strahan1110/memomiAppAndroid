package com.example.apkMemomi.ui.registroEven

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.apkMemomi.R
import com.example.apkMemomi.databinding.RegistroevenBinding
import com.example.apkMemomi.objetos.Eventos
import com.example.apkMemomi.objetos.RetrofitClient
import com.example.apkMemomi.objetos.SharedPrefManager
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class registroeventFragment : Fragment() {

    private var _binding: RegistroevenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = RegistroevenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupListeners()

        return root
    }

    private fun setupListeners() {
        binding.registerEventButton.setOnClickListener {
            val nombre = binding.eventName.text.toString()
            val fecha = binding.etFecha.text.toString()

            if (validateInputs(nombre, fecha)) {//verifica que nombre y fecha no esten vacios
                /*val idEvento = "E0023"
                val idPlan = "C0003"
                val codigo = "EVT25"*/
                registrarEvento(nombre, fecha)
            }
        }

        binding.etFecha.setOnClickListener {
            showDatePicker()
        }

        binding.eventCard.setOnClickListener {
            findNavController().navigate(R.id.nav_listaeven)
        }

    }

    private fun validateInputs(nombre: String, fecha: String): Boolean {
        var isValid = true

        if (nombre.isEmpty()) {
            binding.containerNomev.helperText = "Ingrese un nombre de evento"
            Toast.makeText(context, "Ingrese un nombre de evento", Toast.LENGTH_SHORT).show()
            isValid = false
        } else {
            binding.containerNomev.helperText = null
        }

        if (fecha.isEmpty()) {
            binding.containerFecev.helperText = "Ingrese una fecha de evento"
            Toast.makeText(context, "Ingrese una fecha de evento", Toast.LENGTH_SHORT).show()
            isValid = false
        } else {
            binding.containerFecev.helperText = null
        }

        return isValid
    }

    private fun showDatePicker() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona una fecha")
            .build()

        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = dateFormat.format(Date(selection))
            binding.etFecha.setText(selectedDate)
        }
    }

    private fun registrarEvento(nombre: String, fecha: String) {
        val evento = Eventos(
            IdEvento = "string",
            NombreEvento = nombre,
            FechaEvento = fecha,
            idNovio = SharedPrefManager.obtener(requireContext(),"idUsuario","sin valor").toString() //este valor se debe modificar, jalando con usuario y contraseña desde la api.
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.agregarEvento(evento)
                withContext(Dispatchers.Main) {


                    if (response.isSuccessful) {
                        Toast.makeText(context, "Evento registrado exitosamente!!!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al registrar el evento $response", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                showError("Error de conexión: ${e.message}")
            } catch (e: HttpException) {
                showError("Error HTTP: ${e.message}")
            }
        }
    }

    private suspend fun showError(message: String) = withContext(Dispatchers.Main) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
