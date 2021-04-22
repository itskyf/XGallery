package com.team02.xgallery.ui.adapter

import android.content.ContentUris
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.data.entity.CloudMedia
import com.team02.xgallery.databinding.ListItemMediaBinding
import com.team02.xgallery.utils.AppConstants

class CloudMediaAdapter(
    private val onClick: (CloudMedia) -> Unit,
    private val selectionManager: SelectionManager
) :
    PagingDataAdapter<CloudMedia, CloudMediaAdapter.CloudMediaViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CloudMediaAdapter.CloudMediaViewHolder {
        return CloudMediaAdapter.CloudMediaViewHolder(
            ListItemMediaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CloudMediaViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClick, this, position)
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

    class CloudMediaViewHolder(
        val binding: ListItemMediaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            media: CloudMedia,
            onClick: (CloudMedia) -> Unit,
            adapter: CloudMediaAdapter,
            position: Int
        ) {
            binding.root.setOnClickListener {
                if (adapter.selectionManager.onSelectionMode.value) {
                    adapter.selectionManager.select(media.id!!)
                    adapter.notifyItemChanged(position)

                    if (!adapter.selectionManager.onSelectionMode.value) {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    onClick(media)
                }
            }

            binding.root.setOnLongClickListener {
                adapter.selectionManager.select(media.id!!)
                if (adapter.selectionManager.selectedCount.value == 1)
                    adapter.notifyDataSetChanged()
                else
                    adapter.notifyItemChanged(position)
                true
            }

            if (adapter.selectionManager.onSelectionMode.value) {
                binding.checkBox.isChecked = adapter.selectionManager.isSelected(media.id!!)
                binding.checkBox.visibility = View.VISIBLE
            } else {
                binding.checkBox.visibility = View.GONE
            }

            val mediaRef = Firebase.storage.getReference(media.id!!)
            mediaRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    Glide.with(binding.root).load(it.result).into(binding.imageView)
                }
            }
        }
    }
}