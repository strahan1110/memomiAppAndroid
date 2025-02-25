package com.example.apkMemomi.ui.dshMultimedia

import VideoAdapter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apkMemomi.databinding.DshVideosBinding
import com.example.apkMemomi.objetos.RetrofitClient
import com.example.apkMemomi.objetos.Videos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class video_dsh_Fragment : Fragment(){

    // Declaración del View Binding
    private var _binding: DshVideosBinding? = null
    private val binding get() = _binding!!





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el layout usando el binding
        _binding = DshVideosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnAddVideos.setOnClickListener {
            pickVideoLauncher.launch("video/*")
        }


        /*binding.rclVideos.layoutManager = LinearLayoutManager(context)
        binding.rclVideos.adapter = VideoAdapter(videoList)*/

        ObtenerVideos()

        binding.rclVideos.layoutManager = LinearLayoutManager(context)
        binding.rclVideos.adapter = VideoAdapter(videoList)



    }


    private val pickVideoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Aquí obtendrás el URI del video seleccionado
            uploadVideoToServer(it)
        }
    }

    private fun uploadVideoToServer(uri: Uri) {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.let {
            val bytes = it.readBytes()
            it.close()

            // Obtener el nombre del archivo original y la extensión
            val fileName = getFileName(uri)

            //convertir video en base64
            val encodedVideo = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

            // Crear el objeto del video (ajustar según tu modelo)
            val archivo = Videos(
                IdVideo = "",
                IdEvento = "EV0001",
                Videos = fileName,
                VideoFisic = "",
                InvitadoDNI = "71025677",
                NombreInvitado = "Michael Moises",
                VideoBS64 = encodedVideo
            )

            // Subir el video al servidor
            postVideo(archivo)
        }
    }


    private fun postVideo(archivo: Videos) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.postVideo(listOf(archivo))

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        videoList.clear()

                        //binding.rclVideos.adapter?.notifyItemInserted(videoList.size -1)
                        binding.rclVideos.adapter?.notifyDataSetChanged()
                        ObtenerVideos()
                        Toast.makeText(requireContext(), "Video subido con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error al subir el video", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.RsptMessage.text="error: $e"
                }
            }
        }


    }




    val videoList = mutableListOf<Videos>()
    private fun ObtenerVideos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getVideos()
                if (response.isSuccessful && response.body() != null) {
                    val videos = response.body() ?: emptyList()
                    videoList.clear()
                    videoList.addAll(videos)

                    withContext(Dispatchers.Main) {
                        binding.rclVideos.adapter?.notifyDataSetChanged()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


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

