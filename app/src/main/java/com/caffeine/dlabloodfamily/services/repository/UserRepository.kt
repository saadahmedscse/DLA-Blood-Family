package com.caffeine.dlabloodfamily.services.repository

import androidx.lifecycle.MutableLiveData
import com.caffeine.dlabloodfamily.services.model.UserDetails
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserRepository : UserInterface {

    override fun updateData(user: UserDetails, userMutableLiveData: MutableLiveData<DataState<String>>) {
        userMutableLiveData.postValue(DataState.Loading())
        Constants.userReference.child(Constants.auth.uid!!).setValue(user)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    userMutableLiveData.postValue(DataState.Success("User Created Successfully"))
                }
                else{
                    userMutableLiveData.postValue(DataState.Failed(it.exception?.message))
                }
            }
    }

    override suspend fun getUserData(userMutableLiveData: MutableLiveData<DataState<ArrayList<UserDetails>>>) {
        userMutableLiveData.postValue(DataState.Loading())
        val list = ArrayList<UserDetails>()
        Constants.userReference
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (ds in snapshot.children){
                        ds.getValue(UserDetails::class.java)?.let { list.add(it) }
                    }
                    userMutableLiveData.postValue(DataState.Success(list))
                }

                override fun onCancelled(error: DatabaseError) {
                    userMutableLiveData.postValue(DataState.Failed(error.message))
                }

            })
    }

    override suspend fun getMyInfo(userMutableLiveData: MutableLiveData<DataState<UserDetails>>) {
        userMutableLiveData.postValue(DataState.Loading())
        Constants.userReference.child(Constants.auth.uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserDetails::class.java)
                    userMutableLiveData.postValue(DataState.Success(user))
                }

                override fun onCancelled(error: DatabaseError) {
                    userMutableLiveData.postValue(DataState.Failed(error.message))
                }

            })
    }
}