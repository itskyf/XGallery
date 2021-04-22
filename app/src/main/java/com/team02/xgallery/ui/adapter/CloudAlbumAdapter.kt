package com.team02.xgallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.data.entity.CloudAlbum
import com.team02.xgallery.databinding.ListItemAlbumBinding

class CloudAlbumAdapter(
    private val onClick: (CloudAlbum) -> Unit
) : PagingDataAdapter<CloudAlbum, CloudAlbumAdapter.CloudAlbumViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CloudAlbumViewHolder {
        return CloudAlbumViewHolder(
            ListItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CloudAlbumViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClick)
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<CloudAlbum>() {
            override fun areItemsTheSame(oldItem: CloudAlbum, newItem: CloudAlbum): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CloudAlbum, newItem: CloudAlbum): Boolean {
                // TODO this may suck
                return oldItem == newItem
            }
        }
    }

    class CloudAlbumViewHolder(
        val binding: ListItemAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            album: CloudAlbum, onClick: (CloudAlbum) -> Unit
        ) {
            binding.root.setOnClickListener {
                onClick(album)
            }

            binding.albumName.text = album.name

            val thumbnailRef = Firebase.storage.getReference(album.thumbnailId!!)
            thumbnailRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    Glide.with(binding.root).load(it.result).into(binding.thumbnail)
                }
            }
        }
    }
}