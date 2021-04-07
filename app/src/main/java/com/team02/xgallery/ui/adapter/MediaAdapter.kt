package com.team02.xgallery.ui.adapter

import android.content.ContentUris
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.team02.xgallery.Utils
import com.team02.xgallery.data.entity.Media
import com.team02.xgallery.databinding.ListItemMediaBinding

class MediaAdapter(private val onClick: (Media) -> Unit) :
    PagingDataAdapter<Media, MediaAdapter.MediaViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            ListItemMediaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClick)

        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
                // TODO this may suck
                return oldItem == newItem
            }
        }
    }

    class MediaViewHolder(
        val binding: ListItemMediaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(media: Media, onClick: (Media) -> Unit) {
            binding.root.setOnClickListener {
                onClick(media)
            }
            binding.imageView.load(
                ContentUris.withAppendedId(Utils.collection, media.id as Long)
            )
        }
    }
}