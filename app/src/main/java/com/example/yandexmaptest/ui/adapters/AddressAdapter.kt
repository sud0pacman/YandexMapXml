package com.example.yandexmaptest.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.yandexmaptest.databinding.ItemAddressBinding
import com.example.yandexmaptest.source.AddressData

class AddressAdapter(private val addressList: List<AddressData>) : RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {
    lateinit var setOnClickListener: (AddressData, Int) -> Unit

    inner class MyViewHolder(private val binding: ItemAddressBinding) : ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                Log.d("TTT","adapter bosildi ${addressList[adapterPosition]}")
                setOnClickListener.invoke(addressList[adapterPosition], adapterPosition)
            }
        }

        fun onBind(addressData: AddressData) {
            binding.tvAddressName.text = addressData.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = addressList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(addressList[position])
    }
}