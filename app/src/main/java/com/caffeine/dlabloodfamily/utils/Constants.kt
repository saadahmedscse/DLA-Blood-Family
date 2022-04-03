package com.caffeine.dlabloodfamily.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.services.model.BloodModel
import com.caffeine.dlabloodfamily.services.model.UserDetails
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Constants {
    const val app_name = "DLA Blood Family"
    val reference = FirebaseDatabase.getInstance().reference.child(app_name)
    val auth = FirebaseAuth.getInstance()
    val userReference = reference.child("Users")
    val postReference = reference.child("Posts")

    const val SNACK_LONG = Snackbar.LENGTH_LONG
    const val SNACK_SHORT = Snackbar.LENGTH_SHORT
    const val TOAST_LONG = Toast.LENGTH_LONG
    const val TOAST_SHORT = Toast.LENGTH_SHORT

    fun getHorizontalLayoutManager(context : Context) : LinearLayoutManager{
        return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun getVerticalLayoutManager(context : Context) : LinearLayoutManager{
        return LinearLayoutManager(context)
    }

    fun getStateOfString() : MutableLiveData<DataState<String>>{
        return MutableLiveData<DataState<String>>()
    }

    fun getStateOfListOfUserDetails() : MutableLiveData<DataState<ArrayList<UserDetails>>>{
        return MutableLiveData<DataState<ArrayList<UserDetails>>>()
    }

    fun getStateOfListOfBloodMoel() : MutableLiveData<DataState<ArrayList<BloodModel>>>{
        return MutableLiveData<DataState<ArrayList<BloodModel>>>()
    }

    fun getStateOfBloodMoel() : MutableLiveData<DataState<BloodModel>>{
        return MutableLiveData<DataState<BloodModel>>()
    }

    fun getStateOfUserDetails() : MutableLiveData<DataState<UserDetails>>{
        return MutableLiveData<DataState<UserDetails>>()
    }

    fun intentToActivity(activity : Activity, c : Class<*>){
        activity.startActivity(Intent(activity, c))
        activity.finish()
    }

    fun internetAvailable(context : Context) : Boolean{
        var isConnected = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            isConnected = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                activeNetworkInfo?.run {
                    isConnected = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return isConnected
    }

    fun showToast(context : Context, message : String, duration : Int){
        Toast.makeText(context, message, duration).show()
    }

    fun showSnackBar(context : Context, view : View, message: String, duration : Int){

        val snackBar = Snackbar.make(view, message, duration)
        snackBar.setAction("Close") {
            snackBar.dismiss()
        }

        val snackbarView = snackBar.view
        val font = ResourcesCompat.getFont(context, R.font.regular)

        snackbarView.setBackgroundResource(R.drawable.snack_bg)
        val snackText = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val snackActionText = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        snackText.setTextColor(Color.LTGRAY)
        snackText.typeface = font
        snackActionText.typeface = font
        snackActionText.isAllCaps = false
        snackActionText.setTextColor(context.resources.getColor(R.color.colorLightRed))
        snackText.setTextColor(context.resources.getColor(R.color.colorDarkGrey))
        snackBar.show()
    }
}