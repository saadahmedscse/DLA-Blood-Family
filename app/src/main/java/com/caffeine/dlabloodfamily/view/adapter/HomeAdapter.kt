package com.caffeine.dlabloodfamily.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caffeine.dlabloodfamily.databinding.ItemLayoutViewpagerBinding
import com.caffeine.dlabloodfamily.services.model.HomeModel

class HomeAdapter(val list : ArrayList<HomeModel>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(binding : ItemLayoutViewpagerBinding) : RecyclerView.ViewHolder(binding.root){
        val image = binding.image
        val tvOne = binding.tvOne
        val tvTwo = binding.tvTwo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = list[position]
        holder.image.setImageResource(obj.image)
        holder.tvOne.text = obj.tvOne
        holder.tvTwo.text = obj.tvTwo
    }

    override fun getItemCount(): Int {
        return list.size;
    }
}