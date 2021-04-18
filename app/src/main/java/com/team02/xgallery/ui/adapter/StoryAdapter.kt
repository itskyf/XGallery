package com.team02.xgallery.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.team02.xgallery.databinding.ListItemStoryBinding


class StoryAdapter(private val dataSet: List<Int>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ListItemStoryBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = dataSet[position]
        holder.bind(img)
    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(val binding: ListItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(img: Int) {
            binding.storyThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.storyThumbnail.load(img)
            binding.storyThumbnail.setOnClickListener{

            }
        }
    }
}
