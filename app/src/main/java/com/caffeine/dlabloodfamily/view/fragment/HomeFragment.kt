package com.caffeine.dlabloodfamily.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.FragmentHomeBinding
import com.caffeine.dlabloodfamily.services.model.BloodModel
import com.caffeine.dlabloodfamily.services.model.HomeModel
import com.caffeine.dlabloodfamily.utils.AlertDialog
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import com.caffeine.dlabloodfamily.utils.ProgressDialog
import com.caffeine.dlabloodfamily.view.activity.HomeActivity
import com.caffeine.dlabloodfamily.view.adapter.HomeAdapter
import com.caffeine.dlabloodfamily.view.adapter.PostAdapter
import com.caffeine.dlabloodfamily.viewmodel.PostViewModel
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewPagerList : ArrayList<HomeModel>
    private val viewModel : PostViewModel by viewModels()

    private lateinit var posts : ArrayList<BloodModel>
    private lateinit var filteredPost : ArrayList<BloodModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.recyclerView.layoutManager = Constants.getVerticalLayoutManager(requireContext())
        viewPagerList = ArrayList()
        posts = ArrayList()
        filteredPost = ArrayList()
        setupViewPager()

        viewModel.getBloodPosts()

        viewModel.postLiveData.observe(viewLifecycleOwner){
            when (it){
                is DataState.Loading -> {
                    ProgressDialog.showProgressDialog(requireContext())
                }

                is DataState.Success -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        ProgressDialog.dismiss()
                    }, 1000)
                    posts = it.data!!
                    val adapter = PostAdapter(it.data, requireContext())

                    if (adapter.itemCount > 0){
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.noDataLayout.visibility = View.GONE
                        binding.recyclerView.adapter = adapter
                    }
                    else {
                        binding.recyclerView.visibility = View.GONE
                        binding.noDataLayout.visibility = View.VISIBLE
                    }
                }

                is DataState.Failed -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        ProgressDialog.dismiss()
                    }, 1000)
                    AlertDialog.showAlertDialog(requireContext(), it.message!!, "Close")
                    binding.noDataLayout.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterPost(s.toString())
            }

        })

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

    fun filterPost(txt : String){
        filteredPost.clear()
        for (bg in posts){
            if (bg.bg.lowercase(Locale.getDefault()).startsWith(txt.lowercase(Locale.getDefault()))){
                filteredPost.add(bg)
            }
        }

        val adapter = PostAdapter(filteredPost, requireContext())

        if (adapter.itemCount < 1){
            binding.noDataLayout.visibility = View.VISIBLE
        }
        else binding.noDataLayout.visibility = View.GONE

        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setAppBarTitle("Dream Life Association")
    }
}