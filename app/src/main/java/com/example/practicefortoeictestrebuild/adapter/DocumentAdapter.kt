package com.example.practicefortoeictestrebuild.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.databinding.ItemDocumentBinding
import com.example.practicefortoeictestrebuild.model.Document

class DocumentAdapter : RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    private var listDocument: MutableList<Document> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DocumentViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        with(holder) {
            binding.txtTitle.text = listDocument[position].title

            var content = "."
            listDocument[position].contents.forEach {
                if (content == ".")
                    content = "\t\t$it"
                else content += "\n\t\t$it"
            }
            binding.txtContent.text = content
        }
    }

    override fun getItemCount(): Int = listDocument.size

    fun setData(data: MutableList<Document>) {
        listDocument = data
        notifyDataSetChanged()
    }

    class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by lazy { ItemDocumentBinding.bind(view) }
    }
}