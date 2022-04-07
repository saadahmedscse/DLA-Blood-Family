package com.caffeine.dlabloodfamily.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.ItemLayoutDonorBinding
import com.caffeine.dlabloodfamily.services.model.UserDetails
import com.caffeine.dlabloodfamily.utils.AlertDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DonorAdapter(val list: ArrayList<UserDetails>, val context: Context) : RecyclerView.Adapter<DonorAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemLayoutDonorBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.name
        val bg = binding.bg
        val ldd = binding.ldd
        val loc = binding.location
        val messageBtn = binding.messageBtn
        val call = binding.callBtn
        val availability = binding.availability
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutDonorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        val today = sdf.format(Date())
        val days: Long

        holder.name.text = user.name
        holder.bg.text = user.bg
        holder.loc.text = user.location

        val donation = user.ldd

        if (donation == "never"){
            holder.ldd.text = "Never Donated"
            holder.availability.setBackgroundResource(R.drawable.bg_green_circle)
        }
        else {
            try {
                val TODAY = sdf.parse(today)
                val LAST = sdf.parse(user.ldd)
                days = ((TODAY.time - LAST.time) / (1000 * 60 * 60 * 24))

                val d = days.toInt()

                if (d == 0){
                    holder.ldd.text = "Today"
                }
                else if (d == 1){
                    holder.ldd.text = "Yesterday"
                }
                else {
                    holder.ldd.text = "$days days ago"
                }

                if (days < 120){
                    holder.availability.setBackgroundResource(R.drawable.bg_red_circle)
                }
                else {
                    holder.availability.setBackgroundResource(R.drawable.bg_green_circle)
                }
            }
            catch (e : Exception){}
        }

        holder.call.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${user.number}"))
            context.startActivity(intent)
        }

        holder.messageBtn.setOnClickListener{
            val intent = Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + user.number))
            intent.putExtra( "sms_body", "Hello, I need ${user.bg} blood")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}