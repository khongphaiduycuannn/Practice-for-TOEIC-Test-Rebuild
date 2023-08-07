package com.example.practicefortoeictestrebuild.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.databinding.ItemLessonBinding
import com.example.practicefortoeictestrebuild.model.DataOverview
import com.example.practicefortoeictestrebuild.ui.course.CourseViewModel
import com.example.practicefortoeictestrebuild.ui.lesson.LessonViewModel

class LessonAdapter(
    private var listLesson: MutableList<DataOverview> = mutableListOf(),
    private val fragment: Fragment
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    private val viewModel by lazy {
        fragment.activity?.let {
            ViewModelProvider(it)[LessonViewModel::class.java]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun getItemCount(): Int = listLesson.size

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        with(holder) {
            binding.txtTitle.text = listLesson[position].name

            binding.lnLesson.setOnClickListener {
                viewModel?.setId(listLesson[position].id)
                fragment.findNavController().navigate(R.id.action_courseFragment_to_lessonFragment)
            }

            if (listLesson[position].progress == 1)
                binding.imgStatus.setImageResource(R.drawable.ic_checked)
            else binding.imgStatus.setImageResource(R.color.white)
        }
    }

    class LessonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by lazy { ItemLessonBinding.bind(view) }
    }
}