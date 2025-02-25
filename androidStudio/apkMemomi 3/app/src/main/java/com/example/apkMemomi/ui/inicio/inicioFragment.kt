package com.example.apkMemomi.ui.inicio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apkMemomi.databinding.InicioBinding
import com.example.apkMemomi.databinding.FragmentSlideshowBinding

class inicioFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private var _bindingI: InicioBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _bindingI!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingI = InicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}