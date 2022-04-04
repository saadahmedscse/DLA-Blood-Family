package com.caffeine.dlabloodfamily.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caffeine.dlabloodfamily.services.repository.AuthRepository
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState

class AuthViewModel : ViewModel() {

    private val repository : AuthRepository = AuthRepository()

    private val authMutableLiveData : MutableLiveData<DataState<String>> = Constants.getStateOfString()
    val authLiveData : LiveData<DataState<String>>
        get() = authMutableLiveData

    fun sendVerificationCode(activity: Activity, number : String){
        repository.sendOtpCode("+88$number", activity, authMutableLiveData)
    }

    fun verifyOTP(verificationID: String, code : String){
        repository.verifyCode(verificationID, code, authMutableLiveData)
    }

    fun checkData(){
        repository.checkData(authMutableLiveData)
    }
}