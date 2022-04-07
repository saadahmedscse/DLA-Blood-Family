package com.caffeine.dlabloodfamily.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.ActivityHomeBinding
import com.caffeine.dlabloodfamily.utils.AdUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var adUtils: AdUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavView.setupWithNavController(navController)

        adUtils = AdUtils.getInstance(this)
        adUtils.loadVideoAd()
        Handler(Looper.getMainLooper()).postDelayed({
            adUtils.showVideoAd(this)
        }, 10000)
    }

    fun setAppBarTitle(title : String){
        binding.appbar.text = title
    }

    fun showViewAd(){
        adUtils.loadVideoAd()
        Handler(Looper.getMainLooper()).postDelayed({
        }, 1000)
    }
}