package com.team02.xgallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team02.xgallery.databinding.ListItemStoryBinding


class StoryAdapter(private val onClick: () -> Unit, private val dataSet: List<Int>, private val title: List<String>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ListItemStoryBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = dataSet[position]
        val text = title[position]
        holder.bind(onClick, img, text)
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(val binding: ListItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(onClick: () -> Unit, img: Int, text: String) {
            //binding.storyThumbnail.load(img)
            binding.storyName.text = text
            binding.storyThumbnail.setOnClickListener{
                onClick()
            }
        }
    }
}
