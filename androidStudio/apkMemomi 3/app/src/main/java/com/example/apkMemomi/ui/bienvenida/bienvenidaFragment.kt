package com.example.apkMemomi.ui.bienvenida

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import com.example.apkMemomi.Aplicacion
import com.example.apkMemomi.R
import com.example.apkMemomi.databinding.BienvenidaBinding
import com.example.apkMemomi.objetos.Novios


class bienvenidaFragment : Fragment() {

    private var _bindingB: BienvenidaBinding? = null
    private val binding get() = _bindingB!!
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingB = BienvenidaBinding.inflate(inflater, container, false)
        val root: View = binding.root

       // Recupera el nombre del usuario desde el Bundle
        val nombreUsuario1 = arguments?.getString("usuario1")
        val nombreUsuario2 = arguments?.getString("usuario2")

        // Muestra el nombre del usuario en el TextView
        binding.textView3.text = "Bienvenido, $nombreUsuario1 y $nombreUsuario2"


        exoPlayer = SimpleExoPlayer.Builder(root.context).build()

        binding.playerView.player=exoPlayer

        val rawVideoUri= Uri.parse("android.resource://"+root.context.packageName+"/"+ R.raw.intro_boda)

        exoPlayer.setMediaItem(MediaItem.fromUri(rawVideoUri))

        exoPlayer.prepare()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingB = null
    }

    class AudioViewModel:ViewModel(){
        private val _text=MutableLiveData<String>().apply {
            value = "AUDIO"
        }
        val text: LiveData<String> = _text
    }
}