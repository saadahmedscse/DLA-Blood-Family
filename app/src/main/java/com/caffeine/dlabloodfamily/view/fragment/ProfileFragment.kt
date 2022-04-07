package com.caffeine.dlabloodfamily.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.FragmentProfileBinding
import com.caffeine.dlabloodfamily.utils.*
import com.caffeine.dlabloodfamily.view.activity.AuthenticationActivity
import com.caffeine.dlabloodfamily.view.activity.HomeActivity
import com.caffeine.dlabloodfamily.view.adapter.ProfilePostAdapter
import com.caffeine.dlabloodfamily.viewmodel.PostViewModel
import com.caffeine.dlabloodfamily.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val userViewModel : UserViewModel by viewModels()
    private val postViewModel : PostViewModel by viewModels()
    private lateinit var last : String
    private var count = 0

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)

        val adUtils = AdUtils.getInstance(requireContext())
        adUtils.showBannerAd(binding.adView)
        adUtils.loadVideoAd()
        binding.recyclerView.layoutManager = Constants.getVerticalLayoutManager(requireContext())

        userViewModel.getMyInfo()
        postViewModel.getMyPosts()

        postViewModel.myPostLiveData.observe(viewLifecycleOwner){
            when (it) {
                is DataState.Loading -> {
                    ProgressDialog.showProgressDialog(requireContext())
                }

                is DataState.Success -> {

                    val adapter = ProfilePostAdapter(it.data!!)

                    if (adapter.itemCount > 0){
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.noDataLayout.visibility = View.GONE
                        binding.recyclerView.adapter = adapter
                    }
                    else {
                        binding.recyclerView.visibility = View.GONE
                        binding.noDataLayout.visibility = View.VISIBLE
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        ProgressDialog.dismiss()
                    }, 1000)
                }

                is DataState.Failed -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        ProgressDialog.dismiss()
                    }, 1000)
                }
            }
        }

        userViewModel.myLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {}

                is DataState.Success -> {
                    val user = it.data!!
                    binding.bg.text = user.bg
                    binding.name.text = user.name
                    binding.number.text = user.number
                    binding.location.text = user.location
                    last = user.ldd

                    if (last == "never") {
                        binding.ldDate.text = "Never Donated"
                        binding.txtCanDonate.text = "You can donate today!"
                        binding.donateBtn.setBackgroundResource(R.drawable.bg_light_red_5)
                        count = 1
                    } else {
                        binding.ldDate.text = last
                        val sdf = SimpleDateFormat("dd-MMM-yyyy")
                        val today = sdf.format(Date())
                        val days: Long
                        try {
                            val TODAY = sdf.parse(today)
                            val LAST = sdf.parse(user.ldd)
                            days = ((TODAY.time - LAST.time) / (1000 * 60 * 60 * 24))
                            val mainDays = 120 - days

                            if (mainDays > 0) {
                                binding.donateBtn.setBackgroundResource(R.drawable.bg_disabled_button)
                                binding.txtCanDonate.text =
                                    "Wait $mainDays more days to donate again"
                            } else {
                                count = 1
                                binding.txtCanDonate.text = "You can donate today!"
                                binding.donateBtn.setBackgroundResource(R.drawable.bg_light_red_5)
                            }
                        } catch (e: Exception) {
                        }
                    }
                }

                is DataState.Failed -> {
                    AlertDialog.showAlertDialog(requireContext(), it.message!!, "Close")
                }
            }
        }

        binding.donateBtn.setOnClickListener{
            if (count == 1){
                val sdf = SimpleDateFormat("dd-MMM-yyyy")
                Constants.userReference.child(Constants.auth.uid!!).child("ldd")
                    .setValue(sdf.format(Date())).addOnCompleteListener{
                        if (it.isSuccessful){
                            adUtils.showVideoAd(requireActivity())
                            SuccessDialog.showAlertDialog(requireContext(), "Congratulations, you have donated blood today", "Close")
                            count = 0
                        }
                        else {
                            AlertDialog.showAlertDialog(requireContext(), "An error occurred, try again later", "Clsoe")
                            count = 0
                        }
                    }
            }
        }

        binding.logoutBtn.setOnClickListener{
            Constants.auth.signOut()
            Constants.intentToActivity(requireActivity(), AuthenticationActivity::class.java)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setAppBarTitle("Donor Profile")
    }
}