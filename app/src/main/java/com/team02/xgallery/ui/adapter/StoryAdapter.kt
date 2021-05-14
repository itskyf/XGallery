package com.team02.xgallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team02.xgallery.databinding.ListItemStoryBinding


class StoryAdapter(private val onClick: () -> Unit, private val dataSet: List<Int>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemStoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = dataSet[position]
        holder.bind(onClick, img)
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(val binding: ListItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(onClick: () -> Unit, img: Int) {
//            binding.storyThumbnail.load(img)
//            binding.storyThumbnail.setOnClickListener {
//                onClick()
//            }
        }
    }
}
