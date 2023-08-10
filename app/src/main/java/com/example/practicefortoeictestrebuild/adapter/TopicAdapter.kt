package com.example.practicefortoeictestrebuild.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.databinding.ItemTopicBinding
import com.example.practicefortoeictestrebuild.model.DataOverview
import com.example.practicefortoeictestrebuild.ui.course.CourseViewModel
import com.example.practicefortoeictestrebuild.ui.test.TestViewModel
import com.example.practicefortoeictestrebuild.ui.vocabulary.VocabularyViewModel

class TopicAdapter(
    private var listTopic: MutableList<DataOverview> = mutableListOf(),
    private val fragment: Fragment
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private val courseViewModel by lazy {
        fragment.activity?.let {
            ViewModelProvider(it)[CourseViewModel::class.java]
        }
    }

    private val testViewModel by lazy {
        fragment.activity?.let {
            ViewModelProvider(it)[TestViewModel::class.java]
        }
    }

    private val vocabularyViewModel by lazy {
        fragment.activity?.let {
            ViewModelProvider(it)[VocabularyViewModel::class.java]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun getItemCount(): Int = listTopic.size

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        with(holder) {
            binding.txtTitle.text = listTopic[position].name

            binding.lnTopic.setOnClickListener {
                if (courseViewModel?.group?.value == "vocabulary") {
                    vocabularyViewModel?.setTopicId(listTopic[position].id)

                    fragment.findNavController()
                        .navigate(R.id.action_courseFragment_to_flashcardStartFragment)
                } else if (!courseViewModel?.group?.value.isNullOrEmpty()) {
                    val list = mutableListOf<String>()
                    listTopic.forEach { list.add(it.id) }

                    testViewModel?.index?.value = position
                    testViewModel?.setTopicId(list)

                    if (courseViewModel?.group?.value == "test")
                        fragment.findNavController()
                            .navigate(R.id.action_courseFragment_to_realTestFragment)
                    else fragment.findNavController()
                        .navigate(R.id.action_courseFragment_to_testStartFragment)
                }
            }
        }
    }

    class TopicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by lazy { ItemTopicBinding.bind(view) }
    }
}