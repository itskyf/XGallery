package com.team02.xgallery.ui.adapter

import android.content.ContentUris
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.data.entity.CloudAlbum
import com.team02.xgallery.databinding.ListItemAlbumBinding
import com.team02.xgallery.utils.AppConstants

class CloudAlbumAdapter(
) : PagingDataAdapter<CloudAlbum, CloudAlbumAdapter.CloudAlbumViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CloudAlbumAdapter.CloudAlbumViewHolder {
        return CloudAlbumAdapter.CloudAlbumViewHolder(
            ListItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CloudAlbumViewHolder, position: Int) {
        Log.d("testing","hlellodas2")
        getItem(position)?.let {
            holder.bind(it, this, position)
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
            album: CloudAlbum,
            adapter: CloudAlbumAdapter,
            position: Int
        ) {
            Log.d("testing","hlellodas3")
//            binding.root.setOnClickListener {
//                if (adapter.selectionManager.onSelectionMode.value) {
//                    adapter.selectionManager.select(media.id!!)
//                    adapter.notifyItemChanged(position)
//
//                    if (!adapter.selectionManager.onSelectionMode.value) {
//                        adapter.notifyDataSetChanged()
//                    }
//                } else {
//                    onClick(media)
//                }
//            }

//
//            val mediaRef = Firebase.storage.getReference(album.id!!)
//            mediaRef.downloadUrl.addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Glide.with(binding.root).load(it.result).into(binding.thumbnail)
//                }
//            }
//            val albumRef = Firebase.storage.getReference(())
            Log.d("testing", album.Name.toString())
        }
    }
}