package com.example.apkMemomi.ui.registrofotos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apkMemomi.databinding.FragmentFotoBinding
import com.example.apkMemomi.objetos.Fotos
import com.example.apkMemomi.objetos.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class fotoFragment : Fragment() {
    /*private var _binding: FragmentFotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var fotoAdapter: FotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFotoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*fotoAdapter = FotoAdapter(this, Fotos)
        binding.recyclerViewFotos.apply {
            adapter = fotoAdapter
            layoutManager = GridLayoutManager(context, 2)
        }*/

        cargarFotos()
        return root
    }

    private fun cargarFotos() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getFotos()

                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) {
                            fotoAdapter.updateList(it)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}

