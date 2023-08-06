package com.example.practicefortoeictestrebuild.ui.test

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentTestStartBinding
import com.example.practicefortoeictestrebuild.utils.startLoading

class TestStartFragment :
    BaseFragment<FragmentTestStartBinding>(FragmentTestStartBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[TestViewModel::class.java]
        }
    }

    private val loadingDialog by lazy { context?.let { Dialog(it) } }

    private var countAllQuestion = 0

    private var countCorrectQuestion = 0

    private var countWrongQuestion = 0

    override fun initData() {
        viewModel?.getData()
    }

    override fun handleEvent() {
        binding.testResultBar.btnNextPractice.setOnClickListener {
            viewModel?.index?.value = viewModel?.index?.value!! + 1
            if (viewModel?.index?.value!! < viewModel?.topicIds?.value!!.size) {
                viewModel?.getData()
            }
            else findNavController().popBackStack()
        }
    }

    override fun bindData() {
        viewModel?.isLoading?.observe(viewLifecycleOwner) {
            if (it) {
                loadingDialog?.startLoading(false)
            } else {
                loadingDialog?.dismiss()
            }
        }

        viewModel?.allQuestion?.observe(this) {
            countAllQuestion = it.size
            binding.testResultBar.txtCountAll.text = "${it.size}"
        }

        viewModel?.correctQuestion?.observe(this) {
            countCorrectQuestion = it.size
            binding.testResultBar.txtCountCorrect.text = "${it.size}"
        }

        viewModel?.wrongQuestion?.observe(this) {
            countWrongQuestion = it.size
            binding.testResultBar.txtCountIncorrect.text = "${it.size}"
            binding.testResultBar.txtCountNew.text =
                "${countAllQuestion - countCorrectQuestion - countWrongQuestion}"

            if (countAllQuestion > 0) {
                val progress: Int = (100.0 * countCorrectQuestion / countAllQuestion).toInt()
                binding.testProgressCard.txtProgress.text = "$progress%"
            }
        }

        viewModel?.topicName?.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
        }
    }
}