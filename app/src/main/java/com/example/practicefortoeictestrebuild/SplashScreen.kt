package com.example.practicefortoeictestrebuild

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.practicefortoeictestrebuild.base.BaseActivity
import com.example.practicefortoeictestrebuild.databinding.ActivitySplashScreenBinding

class SplashScreen :
    BaseActivity<ActivitySplashScreenBinding>(ActivitySplashScreenBinding::inflate) {

    override fun initData() {

    }

    override fun handleEvent() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_zoom_out);
                finish()
            },
            1500
        )
    }

    override fun bindData() {

    }
}