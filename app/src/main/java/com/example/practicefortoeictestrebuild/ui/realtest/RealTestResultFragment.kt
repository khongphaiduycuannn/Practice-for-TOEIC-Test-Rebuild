package com.example.practicefortoeictestrebuild.ui.realtest

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentRealTestResultBinding
import com.example.practicefortoeictestrebuild.ui.test.TestResultViewModel
import com.example.practicefortoeictestrebuild.ui.test.TestViewModel
import java.time.Duration

class RealTestResultFragment :
    BaseFragment<FragmentRealTestResultBinding>(FragmentRealTestResultBinding::inflate) {

    private val testViewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[TestViewModel::class.java]
        }
    }

    private val testResultViewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[TestResultViewModel::class.java]
        }
    }

    private val realTestViewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[RealTestViewModel::class.java]
        }
    }

    private var countAllQuestion = 0

    private var countCorrectQuestion = 0

    private var countWrongQuestion = 0

    override fun initData() {

    }

    override fun handleEvent() {
        binding.testDetailCard.btnReview.setOnClickListener {
            realTestViewModel?.status = "review"
            testResultViewModel?.resetListQuestions()
            findNavController().navigate(R.id.action_realTestResultFragment_to_realTestFragment)
        }

        binding.testDetailCard.btnReplay.setOnClickListener {
            realTestViewModel?.status = "do-test"
            testResultViewModel?.clearQuestions()
            testViewModel?.clearCorrect()
            findNavController().navigate(R.id.action_realTestResultFragment_to_realTestFragment)
        }

        binding.btnNextPractice.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        val startTime = testResultViewModel?.startTime
        val endTime = testResultViewModel?.endTime
        val duration = Duration.between(startTime, endTime)
        binding.testDetailCard.txtCountTotalTime.text = "${formattedTime(duration)}"

        testViewModel?.allQuestion?.observe(viewLifecycleOwner) {
            countAllQuestion = it.size
            binding.testDetailCard.txtCountTotalQuestion.text = "${it.size}"
        }

        testViewModel?.correctQuestion?.observe(viewLifecycleOwner) {
            countCorrectQuestion = it.size
            binding.testDetailCard.txtCountCorrect.text = "${it.size}"

            if (countAllQuestion > 0) {
                val progress: Int = (100.0 * it.size / countAllQuestion).toInt()
                binding.testDetailCard.txtProgress.text = "$progress%"
            }
        }

        testViewModel?.wrongQuestion?.observe(viewLifecycleOwner) {
            binding.testDetailCard.txtCountIncorrect.text = "${it.size}"

            if (countAllQuestion > 0) {
                val progress: Int = (100.0 * countCorrectQuestion / countAllQuestion).toInt()
                binding.testDetailCard.txtProgress.text = "$progress%"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bindData()
    }

    private fun formattedTime(duration: Duration): String {
        val hours = duration.toHours().toString().padStart(2, '0')
        val minutes = (duration.toMinutes() % 60).toString().padStart(2, '0')
        val seconds = (duration.seconds % 60).toString().padStart(2, '0')

        if (hours == "00")
            return "$minutes:$seconds"
        return "$hours:$minutes:$seconds"
    }
}