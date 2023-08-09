package com.example.practicefortoeictestrebuild.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.databinding.ItemCourseGroupBinding
import com.example.practicefortoeictestrebuild.model.QuestionType
import com.example.practicefortoeictestrebuild.ui.category.QuestionTypeActivity
import com.example.practicefortoeictestrebuild.utils.PopUpNotification

class QuestionTypeAdapter(
    private val activity: Activity
) : RecyclerView.Adapter<QuestionTypeAdapter.QuestionTypeViewHolder>() {

    private var listQuestionType: MutableList<QuestionType> = mutableListOf()

    private lateinit var onClick: () -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionTypeViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_course_group, parent, false)
        return QuestionTypeViewHolder(view)
    }

    override fun getItemCount(): Int = listQuestionType.size

    override fun onBindViewHolder(holder: QuestionTypeViewHolder, position: Int) {
        val currentQuestionType = listQuestionType[position]

        with(holder) {
            binding.imgImage.setImageResource(currentQuestionType.image)
            binding.txtTitle.text = currentQuestionType.title
            binding.txtInformation.text = currentQuestionType.information

            binding.itemCourseGroup.setOnClickListener {
                if (currentQuestionType.information.first().toString() == "0") {
                    onClick()
                }
                else {
                    val intent = Intent(activity, QuestionTypeActivity::class.java)
                    intent.putExtra("title", currentQuestionType.title)
                    intent.putExtra("group", currentQuestionType.group)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(
                        R.anim.transition_zoom_in,
                        R.anim.transition_zoom_out
                    )
                }
            }
        }
    }

    fun setData(data: MutableList<QuestionType>) {
        listQuestionType = data
        notifyDataSetChanged()
    }

    fun setOnClick(onClick: () -> Unit) {
        this.onClick = onClick
    }

    class QuestionTypeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by lazy { ItemCourseGroupBinding.bind(view) }
    }
}