package com.example.practicefortoeictestrebuild.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.databinding.ItemLessonBinding
import com.example.practicefortoeictestrebuild.model.DataOverview

class LessonAdapter(
    private var listLesson: MutableList<DataOverview> = mutableListOf(),
    private val fragment: Fragment
): RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

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
        }
    }

    class LessonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by lazy { ItemLessonBinding.bind(view) }
    }
}