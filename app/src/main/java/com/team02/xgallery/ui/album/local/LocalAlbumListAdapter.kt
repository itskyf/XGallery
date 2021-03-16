package com.team02.xgallery.ui.album.local

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team02.xgallery.R

class LocalAlbumListAdapter: RecyclerView.Adapter<LocalAlbumItemViewHolder>() {
    var data = listOf<Album>(
        Album(R.drawable.thumbnail, "Album 1"),
        Album(R.drawable.thumbnail, "Album 2"),
        Album(R.drawable.thumbnail, "Album 3"),
        Album(R.drawable.thumbnail, "Album 4"),
        Album(R.drawable.thumbnail, "Album 5"),
        Album(R.drawable.thumbnail, "Album 6"),
        Album(R.drawable.thumbnail, "Album 7"),
        Album(R.drawable.thumbnail, "Album 8"),
        Album(R.drawable.thumbnail, "Album 9"))
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalAlbumItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_local_album, parent, false) as View
        return LocalAlbumItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocalAlbumItemViewHolder, position: Int) {
        val item = data[position]
        val imgAlbumThumbnail = holder.albumItemLayout.findViewById<ImageView>(R.id.img_album_thumbnail)
        val txtAlbumName = holder.albumItemLayout.findViewById<TextView>(R.id.txt_album_name)

        imgAlbumThumbnail.setImageResource(item.thumbnailResId)
        txtAlbumName.text = item.name
    }

    override fun getItemCount() = data.size
}
