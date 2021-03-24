package com.team02.xgallery.ui.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.team02.xgallery.data.entity.Media
import com.team02.xgallery.databinding.ListHomeMediaBinding
import javax.inject.Inject

class MediaGridAdapter @Inject constructor(storage: FirebaseStorage) :
    PagingDataAdapter<Media, MediaGridAdapter.ViewHolder>(diffCallback) {

    private val storageRef = storage.reference.child("admin")
    // TODO user

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { media ->
            viewHolder.bind(media)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListHomeMediaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class ViewHolder(private val binding: ListHomeMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(media: Media) {
            // TODO
        }
    }
}