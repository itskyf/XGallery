package com.team02.xgallery.ui.library


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team02.xgallery.R
import com.team02.xgallery.data.entity.Media
import com.team02.xgallery.databinding.ListLibraryMediaBinding
import java.util.*

class ImageAdapter(private val _libraries: ArrayList<Media>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListLibraryMediaBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ListLibraryMediaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lib = _libraries[position]
        holder.binding.image.setImageResource(R.drawable.ic_launcher_foreground)
        holder.binding.textView.text = lib.id
    }

    override fun getItemCount(): Int {
        return _libraries.size
    }


}