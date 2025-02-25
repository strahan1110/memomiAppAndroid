package com.example.apkMemomi.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.apkMemomi.R
import com.example.apkMemomi.databinding.LoginBinding
import com.example.apkMemomi.objetos.RetrofitClient
import com.example.apkMemomi.objetos.SharedPrefManager
import com.example.apkMemomi.objetos.ApiRptaNovio
import com.example.apkMemomi.objetos.user
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class loginFragment : Fragment() {

    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.texregis.setOnClickListener{
            findNavController().navigate(R.id.destinoregistronovios)
        }

        binding.texInvitado.setOnClickListener(){
            findNavController().navigate(R.id.destinoinvitado)
        }

        binding.btnLogin.setOnClickListener {
            val user = binding.etEmail.text.toString().trim()
            val psw = binding.etPassword.text.toString().trim()

            binding.containerUser.setHelperTextColor(root.context.getColorStateList(R.color.rojo))
            binding.containerUser.setHelperText("")
            binding.containerPassword.setHelperTextColor(root.context.getColorStateList(R.color.rojo))
            binding.containerPassword.setHelperText("")

            if (user.isEmpty()) {
                binding.containerUser.setHelperText("El campo usuario está vacío")
                return@setOnClickListener
            } else if (psw.isEmpty()) {
                binding.containerPassword.setHelperText("El campo contraseña está vacío")
                return@setOnClickListener
            }

            autenticaruser(user, psw)
        }

        return root
    }
    private fun autenticaruser(user: String, psw: String) {

        obtenerId(user, psw)
        GlobalScope.launch(Dispatchers.IO) {
            // Intentamos hacer la llamada a la API
            val response = try {
                RetrofitClient.api.obtenerNovios() // Llamada a la API para obtener los usuarios
            } catch (e: IOException) {
                showError("Error de conexión: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                showError("Error HTTP: ${e.message}")
                return@launch
            }

            // Verifica que la respuesta sea exitosa y contiene datos
            if (response.isSuccessful && response.body() != null) {
                val usuarios = response.body() // Obtener la lista de usuarios
                Log.d("Respuesta API", "Usuarios recibidos: $usuarios") // Verificación en Logcat

                // Busca un usuario con el correo y la contraseña que coincidan
                val usuarioValido = usuarios?.find {
                    it.Correo.equals(user, ignoreCase = true) && it.Contraseña.equals(psw, ignoreCase = true)
                }

                // Actualizamos la interfaz de usuario en el hilo principal
                withContext(Dispatchers.Main) {
                    if (usuarioValido != null) {
                        val bundle = Bundle().apply {
                            putString("usuario1", usuarioValido.NombreNovio1) // Pasar el nombre al siguiente fragmento
                            putString("usuario2", usuarioValido.NombreNovio2)
                            putString("idnovios", usuarioValido.IdNovio)
                        }
                        findNavController().navigate(R.id.destinobienvenida, bundle)
                      //  findNavController().navigate(R.id.destinobienvenida)
                        Toast.makeText(requireContext(), "Iniciando sesión con $user", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                // Si la respuesta no fue exitosa o el cuerpo está vacío
                showError("Error: Respuesta no exitosa.")
            }
        }
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

    private fun obtenerId(user: String, psw : String) {

        val usuarioID = user(Correo2 = user, Contraseña2 = psw)

        lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitClient.api.obtenerIdNovio(usuarioID)
            } catch (e: Exception) {
                // Manejar errores de red o excepciones
                withContext(Dispatchers.Main) {
                    showAlert("Error: $e")
                }
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                val usuario = response.body() // Obtienes el objeto ApiRptaNovio
                withContext(Dispatchers.Main) {

                    //val dato = SharedPrefManager.obtener(requireContext(),"idUsuario", "sinValor")

                    SharedPrefManager.guardar(requireContext(), "idUsuario", usuario?.id ?: "")

                    showAlert("ID guardado: ${usuario?.id}")
                }
            } else {
                // Manejar el error si la respuesta no fue exitosa
                withContext(Dispatchers.Main) {
                    showAlert("Error: ${response.message()}")
                }
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
