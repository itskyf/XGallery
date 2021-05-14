package com.team02.xgallery.ui.adapter

import android.content.ContentUris
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team02.xgallery.data.entity.Album
import com.team02.xgallery.databinding.ListItemAlbumBinding
import com.team02.xgallery.utils.AppConstants

class AlbumAdapter(private val onClick: (Album) -> Unit) :
    PagingDataAdapter<Album, AlbumAdapter.AlbumViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ListItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        if (album != null) {
            holder.bind(album, onClick)
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem == newItem
            }
        }
    }

    class AlbumViewHolder(
        val binding: ListItemAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album, onClick: (Album) -> Unit) {
            binding.root.setOnClickListener {
                onClick(album)
            }
            binding.albumName.text = album.name
            if (album.thumbnailId is Long) {
                Glide.with(binding.thumbnail)
                    .load(
                        ContentUris.withAppendedId(
                            AppConstants.COLLECTION, album.thumbnailId as Long
                        )
                    )
                    .into(binding.thumbnail)
            }
        }
    }
}
