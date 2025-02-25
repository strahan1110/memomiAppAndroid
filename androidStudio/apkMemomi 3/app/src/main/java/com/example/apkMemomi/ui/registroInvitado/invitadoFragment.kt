package com.example.apkMemomi.ui.registroInvitado

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.apkMemomi.R
import com.example.apkMemomi.databinding.InvitacionqrBinding
import com.example.apkMemomi.objetos.ListaBlanca
import com.example.apkMemomi.objetos.RetrofitClient
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class invitadoFragment : Fragment() {

    private var _binding: InvitacionqrBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventId: String

    private var dniAnswer:String  = ""//guardara el valor del dni en el fragment
    private var qrAnswer :String =""//guardara el valor del qr en el fragment


    private val SCAN_QR_REQUEST_CODE = 101  // Código para identificar la solicitud de escanear QR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InvitacionqrBinding.inflate(inflater, container, false)
        val root: View = binding.root

        arguments?.let {
            eventId = it.getString("eventId", "")
        }

        binding.validateButton.setOnClickListener {
            val dniInput = binding.dniInput.text.toString().trim()

            if (dniInput.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor ingrese su DNI", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dniAnswer = dniInput

            Validar()
        }

        binding.scanQRCodeButton.setOnClickListener {
            // Utilizar CaptureActivity de ZXing para escanear el QR
            val intent = Intent(requireContext(), CaptureActivity::class.java)
            startActivityForResult(intent, SCAN_QR_REQUEST_CODE)
        }

        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCAN_QR_REQUEST_CODE && resultCode == android.app.Activity.RESULT_OK) {
            val qrData = data?.getStringExtra("SCAN_RESULT") ?: return
            qrAnswer = qrData
        }
    }

    private fun Validar() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.obtenerListaInvitados()
                if (response.isSuccessful && response.body() != null) {
                    val listaInvitados = response.body() ?: emptyList<ListaBlanca>()
                    val invitadoValido = listaInvitados?.find {
                        it.Codigo.equals(qrAnswer, ignoreCase = true) || it.InvitadoDNI.equals(dniAnswer, ignoreCase = true)
                    }
                    withContext(Dispatchers.Main){
                        if(invitadoValido != null){
                            Toast.makeText(requireContext(), "ingresaste al if", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.destinodashboard)
                        }
                    }
                } else {
                    showError("Error al obtener la lista de invitados: ${response.code()}")
                }
            } catch (e: IOException) {
                showError("Error de conexión: ${e.message}")
            } catch (e: HttpException) {
                showError("Error HTTP: ${e.message}")
            }
        }
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

