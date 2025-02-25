package com.example.apkMemomi.ui.micuenta

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apkMemomi.Aplicacion
import com.example.apkMemomi.databinding.MicuentaBinding
import com.example.apkMemomi.objetos.Novios
import com.example.apkMemomi.objetos.RetrofitClient
import com.example.apkMemomi.objetos.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import retrofit2.HttpException
import java.io.IOException
import java.io.StringWriter

class micuentaFragment : Fragment() {

    private var _bindingCuenta: MicuentaBinding? = null
    private val binding get() = _bindingCuenta!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingCuenta = MicuentaBinding.inflate(inflater, container, false)
        val root: View = binding.root


        obtenerDatosUsuario()

        //val xmlResult = convertirObjetoEnXml(obtenerDatosUsuario())


        //binding.XMLobject.text = xmlResult

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingCuenta = null
    }

    fun obtenerDatosUsuario(): Novios{


        var objeto = Novios(IdNovio = null, NombreNovio1 = "prueba", NombreNovio2 = "", Correo = "", Contraseña = "pruea", Telefono = "123456789")

        Toast.makeText(context, "ID: ${SharedPrefManager.obtener(requireContext(),"idUsuario","sin valor").toString()}",Toast.LENGTH_SHORT).show()
        GlobalScope.launch(Dispatchers.IO){

            val response = try {

                RetrofitClient.api.getNoviosById(SharedPrefManager.obtener(requireContext(),"idUsuario","sin valor").toString())

            }catch (e: Exception){
                showAlert("error: $e")
                return@launch
            }

            if(response.isSuccessful){

                withContext(Dispatchers.Main){

                    showAlert("exito : $response")

                }
                val usuario = response.body()

                objeto = Novios(
                    IdNovio = usuario?.IdNovio,
                    NombreNovio1 = usuario?.NombreNovio1.toString(),
                    NombreNovio2 = usuario?.NombreNovio2.toString(),
                    Correo = usuario?.Correo.toString(),
                    Contraseña = usuario?.Contraseña.toString(),
                    Telefono = usuario?.Telefono.toString()
                )
                binding.XMLobject.text = convertirObjetoEnXml(objeto)

            }else{
                showAlert("ERROR : $response")
            }

        }
        return objeto

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


    fun convertirObjetoEnXml(objeto: Novios): String {
        val serializer: Serializer = Persister()
        val writer = StringWriter()

        return try {
            // Serializa el objeto y lo escribe en el writer
            serializer.write(objeto, writer)
            writer.toString() // Retorna el XML como String
        } catch (e: Exception) {
            e.printStackTrace()
            "" // Si ocurre un error, retorna un String vacío
        }
    }
}
