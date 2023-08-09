package com.example.practicefortoeictestrebuild.ui.main

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.adapter.QuestionTypeAdapter
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentCategoryBinding
import com.example.practicefortoeictestrebuild.model.QuestionType
import com.example.practicefortoeictestrebuild.utils.PopUpNotification

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate) {

    private val listQuestionType = mutableListOf(
        QuestionType(
            R.drawable.img_weak_question,
            "Weak Questions",
            "0 questions",
            "1"
        ),
        QuestionType(
            R.drawable.img_familiar_question,
            "Familiar Questions",
            "0 questions",
            "2"
        ),
    )

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[CategoryViewModel::class.java]
        }
    }

    override fun initData() {
        viewModel?.getProgressCardIdReview("1")
        viewModel?.getProgressCardIdReview("2")
    }

    override fun handleEvent() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun bindData() {
        binding.viewHomeHeader.txtTitle.text = "Category"
        binding.viewHomeHeader.txtContent.text = "Your Category Questions"

        val questionTypeAdapter = QuestionTypeAdapter(requireActivity())
        questionTypeAdapter.setData(listQuestionType)
        questionTypeAdapter.setOnClick {
            val popUpNotification = PopUpNotification(requireContext())
            popUpNotification.title.text = "No data"
            popUpNotification.message.text = "No data"
            popUpNotification.show(requireView())
        }

        binding.recyclerView.adapter = questionTypeAdapter

        viewModel?.weakCount?.observe(viewLifecycleOwner) {
            listQuestionType[0].information = "$it questions"
            questionTypeAdapter.setData(listQuestionType)
        }

        viewModel?.familiarCount?.observe(viewLifecycleOwner) {
            listQuestionType[1].information = "$it questions"
            questionTypeAdapter.setData(listQuestionType)
        }
    }

    override fun onResume() {
        initData()
        super.onResume()
    }
}