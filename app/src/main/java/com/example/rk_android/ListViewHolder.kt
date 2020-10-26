package com.example.rk_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class ListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {
    private val parent = parent
    private var mTextViewDate: TextView?  = null
    private var mTextViewClosePrice: TextView?  = null
    private var mTextViewCurrency: TextView?  = null

    init {
        mTextViewDate = itemView.findViewById(R.id.date)
        mTextViewClosePrice = itemView.findViewById(R.id.close_price)
        mTextViewCurrency = itemView.findViewById(R.id.currency)
    }

    fun bind(elem: ListAdapter.Elem) {
        mTextViewDate?.text = elem.date
        mTextViewClosePrice?.text = elem.closePrice
        mTextViewCurrency?.text = elem.currency
        itemView.setOnClickListener {
            val itemInfo = secondFragment()
            val bundle = Bundle()
            itemInfo.arguments = bundle
            bundle.putString("high", elem.high)
            bundle.putString("low", elem.low)
            parent.findNavController().navigate(R.id.action_hostFragment_to_secondFragment2, bundle)
        }
    }

}