package com.caffeine.dlabloodfamily.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caffeine.dlabloodfamily.services.model.UserDetails
import com.caffeine.dlabloodfamily.services.repository.UserRepository
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel(){

    private val repository = UserRepository()

    private val userMutableLiveData = Constants.getStateOfString()
    val userLiveData : LiveData<DataState<String>>
        get() = userMutableLiveData

    private val donorMutableLiveData = Constants.getStateOfListOfUserDetails()
    val donorLiveData : LiveData<DataState<ArrayList<UserDetails>>>
        get() = donorMutableLiveData

    private val myMutableLiveData = Constants.getStateOfUserDetails()
    val myLiveData : LiveData<DataState<UserDetails>>
        get() = myMutableLiveData

    fun updateData(user : UserDetails){
        repository.updateData(user, userMutableLiveData)
    }

    fun getDonors(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserData(donorMutableLiveData)
        }
    }

    fun getMyInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMyInfo(myMutableLiveData)
        }
    }
}