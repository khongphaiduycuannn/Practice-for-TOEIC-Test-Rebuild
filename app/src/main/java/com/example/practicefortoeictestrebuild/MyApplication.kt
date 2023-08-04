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
    }
}