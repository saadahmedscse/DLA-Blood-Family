package com.caffeine.dlabloodfamily.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.FragmentHomeBinding
import com.caffeine.dlabloodfamily.services.model.HomeModel
import com.caffeine.dlabloodfamily.view.activity.HomeActivity
import com.caffeine.dlabloodfamily.view.adapter.HomeAdapter

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewPagerList : ArrayList<HomeModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        viewPagerList = ArrayList()
        setupViewPager()

        return binding.root
    }

    private fun setupViewPager(){
        viewPagerList.add(HomeModel(
            R.drawable.clock,
            "If you are a donor, you can only donate blood after 120 days you donated",
            "Each post will appear for three days and after that the post will be deleted"
        ))
        viewPagerList.add(HomeModel(
            R.drawable.contact,
            "If you want to donate blood, you can call or message him through the buttons in post",
            "Or you can call a donor from donor list who has that group of blood to donate"
        ))
        viewPagerList.add(HomeModel(
            R.drawable.blood,
            "If you need blood, you can post for blood and donors will reponse soon",
            "Kindly delete posts of yours from your profile, if you get the blood"
        ))

        val adapter = HomeAdapter(viewPagerList)
        binding.viewPager.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setAppBarTitle("Dream Life Association")
    }
}