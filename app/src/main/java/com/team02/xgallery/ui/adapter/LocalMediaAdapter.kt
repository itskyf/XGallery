package com.team02.xgallery.ui.adapter

import android.content.ContentUris
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team02.xgallery.data.entity.LocalMedia
import com.team02.xgallery.databinding.ListItemMediaBinding
import com.team02.xgallery.utils.AppConstants

class LocalMediaAdapter(
    private val onClick: (LocalMedia) -> Unit,
    private val selectionManager: SelectionManager
) :
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
            holder.bind(it, onClick, this, position)
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
        fun bind(
            media: LocalMedia,
            onClick: (LocalMedia) -> Unit,
            adapter: LocalMediaAdapter,
            position: Int
        ) {
            binding.root.setOnClickListener {
                if (adapter.selectionManager.onSelectionMode.value) {
                    adapter.selectionManager.select(media.id)
                    adapter.notifyItemChanged(position)

                    if (!adapter.selectionManager.onSelectionMode.value) {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    onClick(media)
                }
            }

            binding.root.setOnLongClickListener {
                adapter.selectionManager.select(media.id)
                if (adapter.selectionManager.selectedCount.value == 1)
                    adapter.notifyDataSetChanged()
                else
                    adapter.notifyItemChanged(position)
                true
            }

            if (adapter.selectionManager.onSelectionMode.value) {
                binding.checkBox.isChecked = adapter.selectionManager.isSelected(media.id)
                binding.checkBox.visibility = View.VISIBLE
            } else {
                binding.checkBox.visibility = View.GONE
            }
            Glide.with(binding.imageView)
                .load(
                    ContentUris.withAppendedId(AppConstants.COLLECTION, media.id)
                )
                .into(binding.imageView)
        }
    }
}