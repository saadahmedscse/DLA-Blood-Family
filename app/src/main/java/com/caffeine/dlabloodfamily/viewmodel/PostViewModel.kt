package com.caffeine.dlabloodfamily.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caffeine.dlabloodfamily.services.model.BloodModel
import com.caffeine.dlabloodfamily.services.repository.PostRepository
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val bloodMutableLiveData = Constants.getStateOfString()
    val bloodLiveData : LiveData<DataState<String>>
        get() = bloodMutableLiveData

    private val postMutableLiveData = Constants.getStateOfListOfBloodMoel()
    val postLiveData : LiveData<DataState<ArrayList<BloodModel>>>
        get() = postMutableLiveData

    fun postABloodRequest(bloodModel: BloodModel){
        repository.postARequest(bloodModel, bloodMutableLiveData)
    }

    fun getBloodPosts(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPosts(postMutableLiveData)
        }
    }

    fun deletePost(bloodModel: BloodModel){
        repository.deletePost(bloodModel)
    }
}