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
import com.team02.xgallery.databinding.ListItemMediaBinding
import com.team02.xgallery.utils.GlideApp

class SelectPhotosAdapter(
    private val selectionManager: SelectionManager
) :
    PagingDataAdapter<CloudMedia, SelectPhotosAdapter.SelectPhotosViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectPhotosAdapter.SelectPhotosViewHolder {
        return SelectPhotosAdapter.SelectPhotosViewHolder(
            ListItemMediaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectPhotosViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, this, position)
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

    class SelectPhotosViewHolder(
        val binding: ListItemMediaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            media: CloudMedia,
            adapter: SelectPhotosAdapter,
            position: Int
        ) {
            binding.root.setOnClickListener {
                adapter.selectionManager.select(media.id!!)
                adapter.notifyItemChanged(position)

                if (!adapter.selectionManager.onSelectionMode.value) {
                    adapter.notifyDataSetChanged()
                }
            }

            binding.checkBox.isChecked = adapter.selectionManager.isSelected(media.id!!)
            binding.checkBox.visibility = View.VISIBLE
            GlideApp.with(binding.imageView)
                .load(Firebase.storage.getReference(media.id))
                .into(binding.imageView)
        }
    }
}