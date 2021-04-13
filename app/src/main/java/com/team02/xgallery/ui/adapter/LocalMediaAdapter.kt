package com.team02.xgallery.ui.adapter

import android.content.ContentUris
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.team02.xgallery.data.entity.LocalMedia
import com.team02.xgallery.databinding.ListItemMediaBinding
import com.team02.xgallery.utils.AppConstants

class LocalMediaAdapter(private val onClick: (LocalMedia) -> Unit) :
    PagingDataAdapter<LocalMedia, LocalMediaAdapter.MediaViewHolder>(diffCallback) {
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
        val diffCallback = object : DiffUtil.ItemCallback<LocalMedia>() {
            override fun areItemsTheSame(oldItem: LocalMedia, newItem: LocalMedia): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LocalMedia, newItem: LocalMedia): Boolean {
                // TODO this may suck
                return oldItem == newItem
            }
        }
    }

    class MediaViewHolder(
        val binding: ListItemMediaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(media: LocalMedia, onClick: (LocalMedia) -> Unit) {
            binding.root.setOnClickListener {
                onClick(media)
            }
            binding.imageView.load(
                ContentUris.withAppendedId(AppConstants.COLLECTION, media.id)
            )
        }
    }
}