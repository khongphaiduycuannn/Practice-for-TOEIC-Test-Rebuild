package com.example.practicefortoeictestrebuild

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.practicefortoeictestrebuild.base.BaseActivity
import com.example.practicefortoeictestrebuild.databinding.ActivitySplashScreenBinding
import com.example.practicefortoeictestrebuild.model.AlarmItem
import com.example.practicefortoeictestrebuild.model.AlarmScheduler
import com.example.practicefortoeictestrebuild.ui.main.MainActivity

class SplashScreen :
    BaseActivity<ActivitySplashScreenBinding>(ActivitySplashScreenBinding::inflate) {

    override fun initData() {
        val setting = applicationContext.getSharedPreferences("app_status", 0)
        if (!setting.getBoolean("scheduleNotification", false)) {
            setting.edit().putBoolean("scheduleNotification", true).apply()
            val alarmScheduler = AlarmScheduler(this)
            alarmScheduler.schedule(AlarmItem(getString(R.string.app_name)))
        }
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