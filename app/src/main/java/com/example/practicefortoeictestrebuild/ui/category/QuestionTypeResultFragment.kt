package com.example.practicefortoeictestrebuild.ui.category

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentQuestionTypeResultBinding
import java.time.Duration
import java.time.LocalDateTime

class QuestionTypeResultFragment :
    BaseFragment<FragmentQuestionTypeResultBinding>(FragmentQuestionTypeResultBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[QuestionTypeViewModel::class.java]
        }
    }

    private var countAllQuestion = 0

    private var countCorrectQuestion = 0

    private var countWrongQuestion = 0

    override fun initData() {
        viewModel?.endTime = LocalDateTime.now()
    }

    override fun handleEvent() {
        binding.testDetailCard.btnReview.setOnClickListener {
            viewModel?.resetListQuestions()
            findNavController().navigate(R.id.action_questionTypeResultFragment_to_questionTypeFragment)
        }

        binding.testDetailCard.btnReplay.setOnClickListener {
            viewModel?.clearQuestions()
            findNavController().navigate(R.id.action_questionTypeResultFragment_to_questionTypeFragment)
        }

        binding.btnNextPractice.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun bindData() {
        val startTime = viewModel?.startTime
        val endTime = viewModel?.endTime
        val duration = Duration.between(startTime, endTime)
        binding.testDetailCard.txtCountTotalTime.text = "${formattedTime(duration)}"

        viewModel?.questionIds?.observe(viewLifecycleOwner) {
            countAllQuestion = it.size
            binding.testDetailCard.txtCountTotalQuestion.text = "${it.size}"
        }

        viewModel?.correctCount?.observe(viewLifecycleOwner) {
            countCorrectQuestion = it
            binding.testDetailCard.txtCountCorrect.text = "$it"

            if (countAllQuestion > 0) {
                val progress: Int = (100.0 * it / countAllQuestion).toInt()
                binding.testDetailCard.txtProgress.text = "$progress%"
            }
        }

        viewModel?.incorrectCount?.observe(viewLifecycleOwner) {
            binding.testDetailCard.txtCountIncorrect.text = "$it"

            if (countAllQuestion > 0) {
                val progress: Int = (100.0 * countCorrectQuestion / countAllQuestion).toInt()
                binding.testDetailCard.txtProgress.text = "$progress%"
            }
        }
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