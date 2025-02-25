package com.example.apkMemomi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apkMemomi.databinding.ItemEventoBinding
import com.example.apkMemomi.objetos.Eventos

class EventoAdapter(
    private var eventosList: List<Eventos>,
    private val onItemClick: (Eventos) -> Unit
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(private val binding: ItemEventoBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(eventosList[position]) // Llamar al listener con el evento
                }
            }
        }

        fun bind(evento: Eventos) {
            binding.tvNombreEvento.text = evento.NombreEvento
            binding.tvFechaEvento.text = evento.FechaEvento
            Log.d("BIND", "Binding evento: ${evento.NombreEvento}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(eventosList[position])

    }

    override fun getItemCount() = eventosList.size

    fun updateList(newList: List<Eventos>) {
        Log.d("UPDATE_LIST", "List size: ${newList.size}")
        eventosList = newList
        notifyDataSetChanged()
    }
}
