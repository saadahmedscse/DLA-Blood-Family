package com.caffeine.dlabloodfamily.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.utils.Constants

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            Constants.intentToActivity(this, AuthenticationActivity::class.java)
        }, 0)
    }
}