package com.example.practicefortoeictestrebuild.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.databinding.ItemTopicBinding
import com.example.practicefortoeictestrebuild.model.DataOverview

class TopicAdapter(
    private var listTopic: MutableList<DataOverview> = mutableListOf(),
    private val fragment: Fragment
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

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
        }
    }

    class TopicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by lazy { ItemTopicBinding.bind(view) }
    }
}