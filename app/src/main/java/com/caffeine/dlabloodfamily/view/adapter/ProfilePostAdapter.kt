package com.caffeine.dlabloodfamily.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caffeine.dlabloodfamily.databinding.ItemLayoutProfilePostBinding
import com.caffeine.dlabloodfamily.services.model.BloodModel
import com.caffeine.dlabloodfamily.utils.Constants

class ProfilePostAdapter(val list : ArrayList<BloodModel>) : RecyclerView.Adapter<ProfilePostAdapter.ViewHolder>(){

    class ViewHolder(binding : ItemLayoutProfilePostBinding) : RecyclerView.ViewHolder(binding.root){
        val bg = binding.bg
        val name = binding.name
        val delete = binding.deleteBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutProfilePostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = list[position]
        holder.bg.text = obj.bg
        holder.name.text = obj.name
        holder.delete.setOnClickListener{
            Constants.postReference.child(Constants.auth.uid!!).child(obj.id).removeValue()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}