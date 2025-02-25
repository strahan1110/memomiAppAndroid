package com.example.apkMemomi.ui.dshMultimedia
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apkMemomi.R
import com.example.apkMemomi.databinding.DshComentariosBinding
import com.example.apkMemomi.objetos.Comentarios
import com.example.apkMemomi.objetos.RetrofitClient
import com.google.common.net.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class comentario_dsh_fragment:Fragment() {

    private var _binding: DshComentariosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el layout usando el binding
        _binding = DshComentariosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*Aquí puedes configurar la vista o manejar eventos*/

        binding.btnAddComentarios.setOnClickListener{
            //mostrarDialogo()

            val mensaje = Comentarios(
                IdComentario = "string",  // Asegúrate de proporcionar valores adecuados
                IdEvento = "EV0001",//CAMBIAR VALOR SI ES QUE SE PUEDE HACERLO DINAMICO
                Comentario = "prueba",//etMensaje.text.toString(),
                InvitadoDNI = "string",
                NombreInvitado = "string"
            )


            GlobalScope.launch(Dispatchers.IO) {
                val response = try{
                    RetrofitClient.api.enviarMensaje(mensaje)
                }catch (e: Exception){
                    showAlert("error $e")
                }catch (e: IOException){
                    showAlert("error $e")
                }catch (e: HttpException){
                    showAlert("error $e")
                }

                withContext(Dispatchers.Main){
                    showAlert("$response")
                }

            }

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun mostrarDialogo() {
        // Crea el diálogo
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_comentario, null)
        dialog.setContentView(view)

        // Configura los elementos del diálogo
        val etMensaje = view.findViewById<EditText>(R.id.etMensaje)
        val btnEnviar = view.findViewById<Button>(R.id.btnEnviar)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrar)

        // Acción del botón "Enviar"
        btnEnviar.setOnClickListener {
            val mensaje = Comentarios(
                IdComentario = "string",  // Asegúrate de proporcionar valores adecuados
                IdEvento = "EV0001",//CAMBIAR VALOR SI ES QUE SE PUEDE HACERLO DINAMICO
                Comentario = etMensaje.text.toString(),
                InvitadoDNI = "string",
                NombreInvitado = "string"
            )


            GlobalScope.launch(Dispatchers.IO) {
                val response = try{
                    RetrofitClient.api.enviarMensaje(mensaje)
                }catch (e: Exception){
                    showAlert("error $e")
                }

                showAlert("$response")

            }


            /*if (mensaje.Comentario.isNotEmpty()) {
                enviarMensajeApi(mensaje)
                dialog.dismiss() // Cierra el diálogo
            } else {
                Toast.makeText(requireContext(), "Por favor, escribe un mensaje", Toast.LENGTH_SHORT).show()
            }*/
        }

        // Acción del botón "Cerrar"
        btnCerrar.setOnClickListener {
            dialog.dismiss() // Cierra el diálogo
        }

        // Muestra el diálogo flotante
        dialog.show()
    }

    private fun enviarMensajeApi(mensaje: Comentarios) {

        GlobalScope.launch(Dispatchers.IO) {

            try {
                val response =  RetrofitClient.api.enviarMensaje(mensaje)
                withContext(Dispatchers.Main){
                    showAlert("rpta: ${response}")
                }
            }catch (e: Exception){

            }catch (e: IOException){

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


}