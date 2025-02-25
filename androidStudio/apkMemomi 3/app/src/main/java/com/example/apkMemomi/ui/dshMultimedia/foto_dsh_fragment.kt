package com.example.apkMemomi.ui.dshMultimedia

import PhotoAdapter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.upstream.ParsingLoadable
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apkMemomi.databinding.DshFotosBinding
import com.example.apkMemomi.objetos.Fotos
import com.example.apkMemomi.objetos.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class foto_dsh_fragment: Fragment() {

    private var _binding: DshFotosBinding? = null
    private val binding get() = _binding!!

    val photoList = mutableListOf<Fotos>()

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DshFotosBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Datos de ejemplo
        obtenerFotos()


        binding.btnAddFotos.setOnClickListener{

            pickImageLauncher.launch("image/*")

        }


        // Configura el RecyclerView
        /*binding.textError.text = "contenido de photolist: $photoList"*/
        binding.rclFotos.layoutManager = LinearLayoutManager(context)
        binding.rclFotos.adapter =  PhotoAdapter(photoList)

    }


    private fun obtenerFotos(){ // Función que retornará una lista del objeto Fotos

        //val listaFotos = mutableListOf<Fotos>() // Lista de tipo Fotos
        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
                    val response = RetrofitClient.api.getFotos()
                    /*binding.textError.text = "respuesta del server: $response"*/
                    if (response.isSuccessful && response.body() != null) {
                        val fotos = response.body()
                        /*binding.textRespuestaServer.text = "$fotos"*/
                        fotos?.let {
                            photoList.addAll(it)  // Asumiendo que la respuesta es una lista de Fotos
                            /*binding.textRespuestaServer.text = "Lista fotos: $photoList"*/
                            binding.rclFotos.adapter?.notifyDataSetChanged() //notificamos al adaptador que haga los cambios de las fotos
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejo de errores si la llamada falla
                Log.e("Error", "Error al obtener fotos: ${e.message}")
            }
        }
    }



    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Aquí obtendrás el URI de la imagen seleccionada
            uploadImageToServer(it)
        }
    }


    private fun uploadImageToServer(uri: Uri) {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.let {
            val bytes = it.readBytes()
            it.close()

            // Obtener el nombre del archivo original y la extensión
            val fileName = getFileName(uri)

            // Convertir los bytes de la imagen a base64
            val encodedImage = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

            // Crear el objeto de la imagen
            val archivo = Fotos(
                IdFotos = "string",
                IdEvento = "string",
                Fotos = fileName,
                FotoFisic = "asdasdasd",
                InvitadoDNI = "71025677",
                NombreInvitado = "Michael Moises",
                FotoBS64 = encodedImage,
            )

            // Subir la foto al servidor
            postFoto(archivo)
        }
    }

    private fun postFoto(archivo: Fotos) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.postFotoesArchivo(listOf(archivo))
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Foto subida con éxito
                        // Agregar la nueva foto a la lista de fotos y actualizar el RecyclerView
                        photoList.clear() // Agregar la foto subida a la lista
                        //binding.rclFotos.adapter?.notifyItemInserted(photoList.size - 1) // Notificar al adaptador que se insertó un nuevo elemento

                        binding.rclFotos.adapter?.notifyDataSetChanged()
                        obtenerFotos()


                        // Mostrar mensaje de éxito
                        Toast.makeText(requireContext(), "Imagen subida con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // En caso de error de conexión o cualquier otro error
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /*funciones ayuda*/
    private fun getFileName(uri: Uri): String {
        var fileName: String = "imagen.jpg"  // Valor por defecto

        if (uri.scheme.equals("content")) {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.let {
                val nameIndex = cursor.getColumnIndex("_data")
                if (cursor.moveToFirst()) {
                    // Extraemos el nombre del archivo desde la columna "_data"
                    val filePath = cursor.getString(nameIndex)
                    // Obtenemos el nombre del archivo y la extensión
                    fileName = filePath.substringAfterLast("/")
                }
                cursor.close()
            }
        } else {
            // En caso de que el URI sea de tipo "file", extraemos el nombre del archivo desde el path
            val filePath = uri.path ?: ""
            fileName = filePath.substringAfterLast("/")
        }

        // Si no tiene extensión, podemos agregar una por defecto, como ".jpg"
        if (!fileName.contains(".")) {
            fileName += ".jpg"
        }

        return fileName
    }




}


