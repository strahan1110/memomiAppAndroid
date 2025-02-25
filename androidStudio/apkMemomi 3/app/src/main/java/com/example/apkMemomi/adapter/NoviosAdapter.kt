package com.example.apkMemomi.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apkMemomi.databinding.ItemNovioBinding
import com.example.apkMemomi.objetos.Novios
import com.example.apkMemomi.objetos.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class NoviosAdapter(private var noviosList: MutableList<Novios>) : RecyclerView.Adapter<NoviosAdapter.NoviosViewHolder>() {

    inner class NoviosViewHolder(private val binding: ItemNovioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(novio: Novios) {
            binding.etNombreNovio1.setText(novio.NombreNovio1)
            binding.etNombreNovio2.setText(novio.NombreNovio2)
            binding.etCorreo.setText(novio.Correo)
            binding.etTelefono.setText(novio.Telefono)
            binding.etContrasena.setText(novio.Contraseña)

            // Agregar listeners para detectar cambios y actualizarlos
            binding.etNombreNovio1.addTextChangedListener(createTextWatcher(novio.NombreNovio1) { text ->
                actualizarNovioPorId(novio.NombreNovio1, "NombreNovio1", text)
            })
            binding.etNombreNovio2.addTextChangedListener(createTextWatcher(novio.NombreNovio2) { text ->
                actualizarNovioPorId(novio.NombreNovio2, "NombreNovio2", text)
            })
            binding.etCorreo.addTextChangedListener(createTextWatcher(novio.Correo) { text ->
                actualizarNovioPorId(novio.Correo, "Correo", text)
            })
            binding.etTelefono.addTextChangedListener(createTextWatcher(novio.Telefono) { text ->
                actualizarNovioPorId(novio.Telefono, "Telefono", text)
            })
            binding.etContrasena.addTextChangedListener(createTextWatcher(novio.Contraseña) { text ->
                actualizarNovioPorId(novio.Contraseña, "Contrasena", text)
            })
        }

        private fun createTextWatcher(id: String, onTextChanged: (String) -> Unit): TextWatcher {
            return object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onTextChanged(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            }
        }

        private fun actualizarNovioPorId(id: String, campo: String, valor: String) {
            // Aquí haces la llamada a la API para obtener el Novio por ID y actualizar
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response: Response<Novios> = RetrofitClient.api.getNoviosById(id)
                    if (response.isSuccessful) {
                        val novio = response.body()
                        novio?.let {
                            // Actualiza el campo especificado
                            when (campo) {
                                "NombreNovio1" -> it.NombreNovio1 = valor
                                "NombreNovio2" -> it.NombreNovio2 = valor
                                "Correo" -> it.Correo = valor
                                "Telefono" -> it.Telefono = valor
                                "Contrasena" -> it.Contraseña = valor
                            }
                            // Notificar que el item ha sido actualizado
                            notifyItemChanged(noviosList.indexOf(it))
                        }
                    } else {
                        Log.d("API_ERROR", "Error al obtener Novio por ID")
                    }
                } catch (e: Exception) {
                    Log.d("API_ERROR", "Excepción: ${e.message}")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoviosViewHolder {
        val binding = ItemNovioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoviosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoviosViewHolder, position: Int) {
        holder.bind(noviosList[position])
    }

    override fun getItemCount() = noviosList.size

    fun updateList(newList: List<Novios>) {
        noviosList = newList.toMutableList()
        notifyDataSetChanged()
    }
}
