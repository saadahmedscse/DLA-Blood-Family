package com.caffeine.dlabloodfamily.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caffeine.dlabloodfamily.databinding.ItemLayoutPostBinding
import com.caffeine.dlabloodfamily.services.model.BloodModel
import com.caffeine.dlabloodfamily.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter(val list : ArrayList<BloodModel>, val context : Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemLayoutPostBinding) : RecyclerView.ViewHolder(binding.root){
        val bg = binding.bg
        val name = binding.name
        val desc = binding.desc
        val date = binding.date
        val time = binding.time
        val quantity = binding.quantity
        val hospital = binding.hospital
        val location = binding.location
        val poster = binding.poster
        val call = binding.callBtn
        val message = binding.messageBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blood = list[position]

        holder.bg.text = blood.bg
        holder.name.text = blood.name
        holder.poster.text = blood.poster
        holder.date.text = blood.date
        holder.time.text = blood.time
        holder.desc.text = blood.desc
        holder.quantity.text = blood.quantity
        holder.hospital.text = blood.hospital
        holder.location.text = blood.location

        holder.call.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${blood.number}"))
            context.startActivity(intent)
        }

        holder.message.setOnClickListener{
            val intent = Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + blood.number))
            intent.putExtra( "sms_body", "I can give ${blood.bg} blood in ${blood.hospital}")
            context.startActivity(intent)
        }

        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        val today = sdf.format(Date())
        var days : Long = 0

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = blood.currentTime.toLong()
        val postdate = sdf.format(calendar.time)

        try {
            val TODAY = sdf.parse(today)
            val LAST = sdf.parse(postdate)
            days = ((TODAY.time - LAST.time) / (1000 * 60 * 60 * 24))
        }
        catch (e : Exception){}

        val ref = Constants.postReference.child(blood.uid).child(blood.id)
        if (days > 2){
            ref.removeValue()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}