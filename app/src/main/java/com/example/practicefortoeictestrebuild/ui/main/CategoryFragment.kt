package com.example.practicefortoeictestrebuild.ui.main

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.adapter.QuestionTypeAdapter
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentCategoryBinding
import com.example.practicefortoeictestrebuild.model.QuestionType

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate) {

    private val listQuestionType = mutableListOf(
        QuestionType(
            R.drawable.img_weak_question,
            "Weak Questions",
            "0 question",
            "weak"
        ),
        QuestionType(
            R.drawable.img_familiar_question,
            "Familiar Questions",
            "0 words",
            "familiar"
        ),
    )

    override fun initData() {

    }

    override fun handleEvent() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun bindData() {
        binding.viewHomeHeader.txtTitle.text = "Category"
        binding.viewHomeHeader.txtContent.text = "Your Category Questions"

        binding.recyclerView.adapter = QuestionTypeAdapter(
            listQuestionType, requireActivity()
        )
    }
}