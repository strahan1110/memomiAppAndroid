package com.example.apkMemomi.ui.logout

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apkMemomi.Aplicacion
import com.example.apkMemomi.databinding.LogoutBinding
import com.example.apkMemomi.databinding.FragmentSlideshowBinding

class logoutFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private var _bindingLog: LogoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _bindingLog!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingLog = LogoutBinding.inflate(inflater, container, false)
        val root: View = binding.root




        /*
                val textView: TextView = binding.textSlideshow
                slideshowViewModel.text.observe(viewLifecycleOwner) {
                    textView.text = it
                }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}