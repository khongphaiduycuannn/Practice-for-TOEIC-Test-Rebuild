package com.example.practicefortoeictestrebuild

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MyApplication.context = applicationContext
    }

    companion object {
        private var context: Context? = null

        fun getAppContext(): Context? {
            return context
        }

        fun getToken(): String? {
            val setting = getAppContext()?.getSharedPreferences("app_status", 0)
            return setting?.getString("access_token", null)
        }

        fun setToken(token: String?) {
            val setting = getAppContext()?.getSharedPreferences("app_status", 0)
            setting?.edit()?.putString("access_token", "Bearer $token")?.apply()
        }

        fun clearToken() {
            val setting = getAppContext()?.getSharedPreferences("app_status", 0)
            setting?.edit()?.putString("access_token", null)?.apply()
        }
    }
}