package com.example.practicefortoeictestrebuild.ui.login

import android.view.WindowManager
import com.example.practicefortoeictestrebuild.base.BaseActivity
import com.example.practicefortoeictestrebuild.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun initData() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun handleEvent() {

    }

    override fun bindData() {

    }
}