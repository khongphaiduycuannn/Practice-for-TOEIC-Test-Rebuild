package com.example.practicefortoeictestrebuild.ui.category

import androidx.lifecycle.ViewModelProvider
import com.example.practicefortoeictestrebuild.base.BaseActivity
import com.example.practicefortoeictestrebuild.databinding.ActivityQuestionTypeBinding

class QuestionTypeActivity :
    BaseActivity<ActivityQuestionTypeBinding>(ActivityQuestionTypeBinding::inflate) {

    private val viewModel by lazy {
        ViewModelProvider(this)[QuestionTypeViewModel::class.java]
    }

    override fun initData() {
        viewModel?.getData()
    }

    override fun handleEvent() {

    }

    override fun bindData() {

    }
}