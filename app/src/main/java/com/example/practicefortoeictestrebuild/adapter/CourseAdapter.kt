package com.example.practicefortoeictestrebuild.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.databinding.ItemCourseBinding
import com.example.practicefortoeictestrebuild.model.Course

class CourseAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private var listCourse: MutableList<Course> = mutableListOf()

    var isCollapsed: MutableList<Boolean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCourse.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentCourse = listCourse[position]

        with(holder) {
            binding.seekbar.seekbar.isEnabled = false
            binding.txtTitle.text = currentCourse.title
            if (isCollapsed[position]) {
                binding.imgStatus.setImageResource(R.drawable.ic_more)
                binding.lnBody.layoutParams = getParam(LayoutParams.MATCH_PARENT, 0)
            } else {
                binding.imgStatus.setImageResource(R.drawable.ic_collap)
                binding.lnBody.layoutParams =
                    getParam(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }

            binding.listLesson.adapter =
                listCourse[position].listLessons?.let { LessonAdapter(it, fragment) }
            binding.listTopic.adapter =
                listCourse[position].listTopics?.let { TopicAdapter(it, fragment) }

            binding.linearLayout.setOnClickListener {
                if (!isCollapsed[position]) {
                    binding.imgStatus.setImageResource(R.drawable.ic_more)
                    binding.lnBody.layoutParams = getParam(LayoutParams.MATCH_PARENT, 0)
                } else {
                    binding.imgStatus.setImageResource(R.drawable.ic_collap)
                    binding.lnBody.layoutParams =
                        getParam(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                }

                isCollapsed[position] = !isCollapsed[position]
            }
        }
    }

    fun setData(data: MutableList<Course>) {
        listCourse = data
        if (isCollapsed.size != listCourse.size)
            isCollapsed = MutableList(listCourse.size) { true }
        notifyDataSetChanged()
    }

    fun getParam(width: Int, height: Int): LayoutParams {
        return LayoutParams(
            width, height
        )
    }

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by lazy { ItemCourseBinding.bind(view) }
    }
}