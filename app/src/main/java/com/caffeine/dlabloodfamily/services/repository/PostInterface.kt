package com.caffeine.dlabloodfamily.services.repository

import androidx.lifecycle.MutableLiveData
import com.caffeine.dlabloodfamily.services.model.BloodModel
import com.caffeine.dlabloodfamily.utils.DataState

interface PostInterface {

    fun postARequest(bloodModel: BloodModel, bloodMutableLiveData: MutableLiveData<DataState<String>>)

    suspend fun getPosts(bloodMutableLiveData: MutableLiveData<DataState<ArrayList<BloodModel>>>)

    fun deletePost(bloodModel: BloodModel)

    suspend fun getMyPosts(bloodMutableLiveData: MutableLiveData<DataState<ArrayList<BloodModel>>>)
}