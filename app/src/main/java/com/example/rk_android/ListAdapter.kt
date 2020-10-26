package com.example.rk_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val list: List<Elem>)
    : RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val elemItem: Elem = list[position]
        holder.bind(elemItem)
    }

    override fun getItemCount(): Int = list.size

    data class Elem(val date: String, val closePrice: String, val currency: String, val high:String, val low:String, val open: String)


}

