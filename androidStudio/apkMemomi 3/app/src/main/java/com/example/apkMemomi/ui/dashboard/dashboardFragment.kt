package com.example.apkMemomi.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apkMemomi.R
import com.example.apkMemomi.databinding.DashboardqrBinding
import com.example.apkMemomi.databinding.FragmentSlideshowBinding

class dashboardFragment: Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private var _bindingD: DashboardqrBinding? = null


    private val binding get() = _bindingD!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingD = DashboardqrBinding.inflate(inflater, container, false)
        val root: View = binding.root


        /*Agregar logica del fragmento*/

        binding.cardPhotos.setOnClickListener {
            // Navegar al siguiente fragmento
            findNavController().navigate(R.id.destino_dsh_foto)
        }

        binding.cardVideos.setOnClickListener {
            // Navegar al siguiente fragmento
            findNavController().navigate(R.id.destino_dsh_video)
        }
        binding.cardComments.setOnClickListener {
            // Navegar al siguiente fragmento
            findNavController().navigate(R.id.destino_dsh_comentario)
        }

        binding.cardAudios.setOnClickListener {
            // Navegar al siguiente fragmento
            findNavController().navigate(R.id.destino_dsh_audio)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}