package com.team02.xgallery.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.team02.xgallery.R
import com.team02.xgallery.data.entity.User
import com.team02.xgallery.databinding.ListItemMemberBinding

class MemberAdapter(val context: Context): PagingDataAdapter<User, MemberAdapter.MemberViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(
                context,
                ListItemMemberBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val memberItem = getItem(position)
        if (memberItem != null) {
            holder.bind(memberItem)
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.email == newItem.email
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    class MemberViewHolder(
            val context: Context,
            val binding: ListItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memberItem: User) {
            binding.btnMore.setOnClickListener {
                val popup = PopupMenu(context!!, it)
                popup.menuInflater.inflate(R.menu.menu_cloud_album_members, popup.menu)

                popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                    // Respond to menu item click.
                    when (menuItem.itemId) {
                        R.id.btnRemove -> {
                            Log.d("KCH", "Remove member")
                            true
                        }
                    }
                    true
                }
                popup.setOnDismissListener {
                    // Respond to popup being dismissed.
                }
                // Show the popup menu.
                popup.show()
            }
        }
    }
}