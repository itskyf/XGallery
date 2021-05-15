package com.team02.xgallery.ui.adapter

import android.util.Log
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
        var pos = 0
        if(text == "1 years ago") pos = 1
        if(text == "2 years ago") pos = 2
        if(text == "3 years ago") pos = 3
        if(text == "4 years ago") pos = 4
        if(text == "5 years ago") pos = 5
        holder.bind(onClick, img, text, pos)
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
