package com.team02.xgallery.ui.library


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team02.xgallery.databinding.ImageItemBinding
import java.util.*

class ImageAdapter(private val _context: Context, private val _libraries: ArrayList<Image>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lib = _libraries[position]
        Glide.with(_context)
                .load(lib.getImage())
                .into(holder.binding.image);
        holder.binding.textView.text = lib.getTitle()
    }

    override fun getItemCount(): Int {
        return _libraries.size
    }



}