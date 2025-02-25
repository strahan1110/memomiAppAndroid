package com.example.apkMemomi.ui.bienvenida

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apkMemomi.R
import com.example.apkMemomi.databinding.EntradaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class entradasis: Fragment() {
    private var _binding: EntradaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EntradaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnIngresarNovios.setOnClickListener{
            findNavController().navigate(R.id.iranovios)
        }

        binding.btnIngresarInvitado.setOnClickListener{
            findNavController().navigate(R.id.irainvitados)
        }

        return root
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