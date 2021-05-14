package com.team02.xgallery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.data.entity.CloudMedia
import com.team02.xgallery.data.repository.CloudAlbumRepository
import com.team02.xgallery.databinding.ListItemMediaBinding
import com.team02.xgallery.utils.GlideApp

class AddPhotosAlbumAdapter(
    private val selectionManager: SelectionManager,
    private val albumId: String
) :
    PagingDataAdapter<CloudMedia, AddPhotosAlbumAdapter.AddPhotosAlbumViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddPhotosAlbumAdapter.AddPhotosAlbumViewHolder {
        return AddPhotosAlbumViewHolder(
            ListItemMediaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AddPhotosAlbumViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, albumId, this, position)
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<CloudMedia>() {
            override fun areItemsTheSame(oldItem: CloudMedia, newItem: CloudMedia): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CloudMedia, newItem: CloudMedia): Boolean {
                // TODO this may suck
                return oldItem == newItem
            }
        }
    }

    class AddPhotosAlbumViewHolder(
        val binding: ListItemMediaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            media: CloudMedia,
            albumId: String,
            adapter: AddPhotosAlbumAdapter,
            position: Int
        ) {
            binding.checkBox.visibility = View.VISIBLE
            CloudAlbumRepository().existsPhoto(albumId, media.id.toString()) { existing ->
                if (existing) {
                    binding.root.alpha = 0.5f
                    binding.checkBox.isChecked = true
                } else {
                    binding.root.alpha = 1.0f
                    binding.checkBox.isChecked =
                        adapter.selectionManager.isSelected(media.id!!)
                }
            }
            binding.root.setOnClickListener {
                CloudAlbumRepository().existsPhoto(albumId, media.id.toString()) { existing ->
                    if (!existing) {
                        adapter.selectionManager.select(media.id!!)
                        adapter.notifyItemChanged(position)
                    }
                }
            }
            GlideApp.with(binding.root).load(Firebase.storage.getReference(media.id!!))
                .into(binding.imageView)
        }
    }
}