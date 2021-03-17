package com.team02.xgallery.ui.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.team02.xgallery.R
import org.w3c.dom.Text


class CustomItem internal constructor(data: Array<Int>) : RecyclerView.Adapter<CustomItem.ViewHolder?>() {
    private val mData: Array<Int>
    // inflates the cell layout from xml when needed
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        holder.myImage.setImageResource(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size;
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var myImage: ImageView
        override fun onClick(view: View?) {
            System.out.print("hello world")
        }
        init {
            myImage = itemView.findViewById(R.id.image) as ImageView
        }
    }

//    // convenience method for getting data at click position
//    fun getItem(id: Int): Int {
//        return mData[id]
//    }

//    // allows clicks events to be caught
//    fun setClickListener(itemClickListener: ItemClickListener?) {
//        mClickListener = itemClickListener
//    }
//
//    // parent activity will implement this method to respond to click events
//    interface ItemClickListener {
//        fun onItemClick(view: View?, position: Int)
//    }

    // data is passed into the constructor
    init {
        mData = data
    }

}