package com.team02.xgallery.ui.library


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team02.xgallery.databinding.ImageItemBinding
import java.util.*

class ImageAdapter(private val _libraries: ArrayList<Image>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val inflater = LayoutInflater.from(_context)
        //val heroView = inflater.inflate(R.layout.temp, parent, false)
        return ViewHolder(ImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lib = _libraries[position]

        holder.binding.text!!.text = lib.getTitle()
    }

    override fun getItemCount(): Int {
        return _libraries.size
    }



}