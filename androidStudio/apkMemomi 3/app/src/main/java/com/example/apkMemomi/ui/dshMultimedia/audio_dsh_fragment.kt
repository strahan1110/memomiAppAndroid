package com.example.apkMemomi.ui.dshMultimedia

import AudioAdapter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Audio
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apkMemomi.databinding.DshAudiosBinding
import com.example.apkMemomi.objetos.Audios
import com.example.apkMemomi.objetos.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class audio_dsh_fragment : Fragment(){

    private var _binding: DshAudiosBinding? = null
    private val binding get() = _binding!!

    val AudioList = mutableListOf<Audios>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el layout usando el binding
        _binding = DshAudiosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*Aquí puedes configurar la vista o manejar eventos*/

        binding.btnAddAudios.setOnClickListener{
            //Toast.makeText(context,"Se pulso el boton agregar audios",Toast.LENGTH_SHORT).show()
            pickAudioLauncher.launch("audio/*")
        }

        obtenerAudio()

        binding.rclAudios.layoutManager = LinearLayoutManager(context)
        binding.rclAudios.adapter = AudioAdapter(requireContext(),AudioList)



        return root
    }


    private val pickAudioLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->uri?.let{
            uploadAudioToServer(it)
        }
    }

    private fun uploadAudioToServer(uri: Uri){
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)

        inputStream?.let {

            val bytes = it.readBytes()
            it.close()

            val filename = getFileName(uri)
            val encodedAudio = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

            val audioData = Audios(
                IdAudio = "",
                IdEvento = "EV0001",
                Audio = filename,
                AudioFisc = "prooasdasd",
                InvitadoDNI = "17025677",
                NombreInvitado = "Michael Aragon",
                AudioBS64 = encodedAudio
            )
            postAudio(audioData)
        }
    }

    private fun postAudio(audio: Audios) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.postAudio(listOf(audio))

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Limpia la lista de audios
                        AudioList.clear()
                        binding.rclAudios.adapter?.notifyDataSetChanged()

                        // Ejecuta obtenerAudio para cargar los nuevos datos
                        obtenerAudio()

                        binding.rptaSalida.text = "Audio subido de forma exitosa"
                    } else {
                        binding.rptaSalida.text = "Error de respuesta: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.rptaSalida.text = "Error de conexión: $e"
                }
            }
        }
    }





    private fun obtenerAudio(){

        GlobalScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main){
                    val response = RetrofitClient.api.obtenerAudios()
                    if(response.isSuccessful){
                        val fotos = response.body()

                        fotos?.let{

                            AudioList.addAll(it)
                            binding.rclAudios.adapter?.notifyDataSetChanged()

                        }
                    }
                }
            }catch (e: Exception){

            }
        }


    }



    private fun getFileName(uri: Uri): String {
        var fileName = "audio_default.mp3" // Nombre por defecto

        if (uri.scheme.equals("content")) {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex("_display_name")
                if (it.moveToFirst()) {
                    fileName = it.getString(nameIndex)
                }
            }
        } else {
            fileName = uri.path?.substringAfterLast("/") ?: fileName
        }

        return fileName
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onStop() {//detener audio cuando salgo del layout
        super.onStop()
        (binding.rclAudios.adapter as? AudioAdapter)?.releasePlayer()
    }





}