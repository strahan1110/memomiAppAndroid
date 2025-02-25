package com.example.apkMemomi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apkMemomi.databinding.ItemGuestBinding
import com.example.apkMemomi.objetos.ListaBlanca

class ListaBAdapter(private val invitadosList: List<ListaBlanca>) : RecyclerView.Adapter<ListaBAdapter.GuestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val binding = ItemGuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val invitado = invitadosList[position]
        holder.bind(invitado)
    }

    override fun getItemCount(): Int {
        return invitadosList.size
    }

    class GuestViewHolder(private val binding: ItemGuestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(invitado: ListaBlanca) {
            binding.guestDni.text = invitado.InvitadoDNI
        }
    }
}


