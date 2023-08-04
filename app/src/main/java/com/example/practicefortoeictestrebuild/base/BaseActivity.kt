package com.example.practicefortoeictestrebuild.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<viewBinding : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> viewBinding
) : AppCompatActivity() {

    private var _binding: viewBinding? = null

    protected val binding get() = _binding as viewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater(layoutInflater)
        setContentView(binding.root)

        initData()
        handleEvent()
        bindData()
    }

    abstract fun initData()

    abstract fun handleEvent()

    abstract fun bindData()
}