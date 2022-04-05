package com.caffeine.dlabloodfamily.services.repository

import androidx.lifecycle.MutableLiveData
import com.caffeine.dlabloodfamily.services.model.UserDetails
import com.caffeine.dlabloodfamily.utils.DataState

interface UserInterface {

    fun updateData(user : UserDetails, userMutableLiveData: MutableLiveData<DataState<String>>)

    suspend fun getUserData(userMutableLiveData: MutableLiveData<DataState<ArrayList<UserDetails>>>)

    suspend fun getMyInfo(userMutableLiveData: MutableLiveData<DataState<UserDetails>>)
}