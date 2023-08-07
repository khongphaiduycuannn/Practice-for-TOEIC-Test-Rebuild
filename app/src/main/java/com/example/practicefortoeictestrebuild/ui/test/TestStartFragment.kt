package com.example.practicefortoeictestrebuild.ui.test

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentTestStartBinding
import com.example.practicefortoeictestrebuild.utils.PopUpNotification
import com.example.practicefortoeictestrebuild.utils.startLoading

class TestStartFragment :
    BaseFragment<FragmentTestStartBinding>(FragmentTestStartBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[TestViewModel::class.java]
        }
    }

    private val testResultViewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[TestResultViewModel::class.java]
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
            viewModel?.indexPlusOne()
        }

        binding.testProgressCard.btnPractice.setOnClickListener {
            val listId = viewModel?.allQuestion?.value!!
            if (listId.size == 0) {
                val popUpNotification = PopUpNotification(requireContext())
                popUpNotification.title.text = "Notification"
                popUpNotification.message.text = "This topic doesn't have any question!"
                popUpNotification.show(requireView())
            } else {
                testResultViewModel?.setListIds(listId)
                testResultViewModel?.clearQuestions()
                findNavController().navigate(R.id.action_testStartFragment_to_testFragment)
            }
        }

        viewModel?.index?.observe(viewLifecycleOwner) {
            val size = viewModel?.topicIds?.value!!.size
            if (size > 0) {
                if (it < size) {
                    viewModel?.getData()
                } else
                    findNavController().popBackStack()
            }
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

        viewModel?.allQuestion?.observe(viewLifecycleOwner) {
            countAllQuestion = it.size
            binding.testResultBar.txtCountAll.text = "${it.size}"
        }

        viewModel?.correctQuestion?.observe(viewLifecycleOwner) {
            countCorrectQuestion = it.size
            binding.testResultBar.txtCountCorrect.text = "${it.size}"

            binding.testResultBar.txtCountNew.text =
                "${countAllQuestion - countCorrectQuestion - countWrongQuestion}"

            if (countAllQuestion > 0) {
                val progress: Int = (100.0 * countCorrectQuestion / countAllQuestion).toInt()
                binding.testProgressCard.txtProgress.text = "$progress%"
            }
        }

        viewModel?.wrongQuestion?.observe(viewLifecycleOwner) {
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

    override fun onResume() {
        super.onResume()
        bindData()
    }
}