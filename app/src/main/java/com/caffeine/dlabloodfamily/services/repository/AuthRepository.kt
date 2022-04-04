package com.caffeine.dlabloodfamily.services.repository

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class AuthRepository : AuthInterface {

    private lateinit var verificationID : String

    override fun sendOtpCode(number: String, activity: Activity, authMutableLiveData: MutableLiveData<DataState<String>>) {
        authMutableLiveData.postValue(DataState.Loading())
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(number)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, forceResendingToken)
                    verificationID = verificationId
                    authMutableLiveData.postValue(DataState.Success(verificationId))
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithCredential(phoneAuthCredential, authMutableLiveData)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    authMutableLiveData.postValue(DataState.Failed(e.message))
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun signInWithCredential(credential: PhoneAuthCredential, authMutableLiveData: MutableLiveData<DataState<String>>) {
        authMutableLiveData.postValue(DataState.Loading())
        Constants.auth.signInWithCredential(credential)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    checkData(authMutableLiveData)
                }
                else{
                    authMutableLiveData.postValue(DataState.Failed(it.exception?.message))
                }
            }
    }

    override fun checkData(authMutableLiveData: MutableLiveData<DataState<String>>) {
        Constants.userReference.child(FirebaseAuth.getInstance().uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("uid").getValue(String::class.java) == null){
                        authMutableLiveData.postValue(DataState.Success("noInfo"))
                    }
                    else authMutableLiveData.postValue(DataState.Success("hasInfo"))
                }

                override fun onCancelled(error: DatabaseError) {
                    authMutableLiveData.postValue(DataState.Failed(error.message))
                }

            })
    }

    override fun verifyCode(
        verificationID : String,
        code: String,
        verifyCodeMutableLiveData: MutableLiveData<DataState<String>>
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationID, code)
        signInWithCredential(credential, verifyCodeMutableLiveData)
    }
}