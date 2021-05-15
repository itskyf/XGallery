package com.team02.xgallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.databinding.ListItemStoryBinding
import com.team02.xgallery.utils.GlideApp


class StoryAdapter(private val onClick: (Int) -> Unit, private val dataSet: ArrayList<String>, private val title: ArrayList<String>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

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
        holder.bind(onClick, img, text, position+1)
    }

    override fun getItemCount() = title.size

    class ViewHolder(val binding: ListItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(onClick: (Int) -> Unit, img: String, text: String, year: Int) {
            GlideApp.with(binding.storyThumbnail).load(Firebase.storage.getReference(img)).into(binding.storyThumbnail)
            //binding.storyThumbnail.load(img)
            binding.storyName.text = text
            binding.storyThumbnail.setOnClickListener{
                onClick(year)
            }
        }
    }
}
