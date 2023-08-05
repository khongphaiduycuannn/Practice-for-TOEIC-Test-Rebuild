package com.example.practicefortoeictestrebuild.ui.main

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.adapter.CourseGroupAdapter
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentHomeBinding
import com.example.practicefortoeictestrebuild.model.CourseGroup

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val listCourseGroup = mutableListOf<CourseGroup>(
        CourseGroup(
            R.drawable.ic_green_card_type,
            "TOEIC PRACTICE TEST",
            "1000+ question",
            "practice-test"
        ),
        CourseGroup(
            R.drawable.ic_orange_card_type,
            "TOEIC VOCABULARY",
            "1500+ words",
            "vocabulary"
        ),
        CourseGroup(
            R.drawable.ic_red_card_type,
            "TOEIC GRAMMAR",
            "2000+ questions",
            "grammar"
        ),
        CourseGroup(
            R.drawable.ic_blue_card_type,
            "TEST",
            "10+ tests",
            "test"
        )
    )

    override fun initData() {

    }

    override fun handleEvent() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun bindData() {
        binding.recyclerView.adapter = CourseGroupAdapter(
            listCourseGroup, requireActivity()
        )
    }
}