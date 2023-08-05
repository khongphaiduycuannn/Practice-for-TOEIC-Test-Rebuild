package com.example.practicefortoeictestrebuild.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.databinding.ItemCourseGroupBinding
import com.example.practicefortoeictestrebuild.model.CourseGroup
import com.example.practicefortoeictestrebuild.ui.course.CourseActivity

class CourseGroupAdapter(
    private val listCourseGroup: MutableList<CourseGroup>,
    private val activity: Activity
) : RecyclerView.Adapter<CourseGroupAdapter.CourseGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseGroupViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_course_group, parent, false)
        return CourseGroupViewHolder(view)
    }

    override fun getItemCount(): Int = listCourseGroup.size

    override fun onBindViewHolder(holder: CourseGroupViewHolder, position: Int) {
        val currentCourseGroup = listCourseGroup[position]

        with(holder) {
            binding.imgImage.setImageResource(currentCourseGroup.image)
            binding.txtTitle.text = currentCourseGroup.title
            binding.txtInformation.text = currentCourseGroup.information

            binding.itemCourseGroup.setOnClickListener {
                val intent = Intent(activity, CourseActivity::class.java)
                intent.putExtra("title", currentCourseGroup.title)
                intent.putExtra("group", currentCourseGroup.group)
                activity.startActivity(intent)
                activity.overridePendingTransition(
                    R.anim.transition_zoom_in,
                    R.anim.transition_zoom_out
                )
            }
        }
    }

    class CourseGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by lazy { ItemCourseGroupBinding.bind(view) }
    }
}