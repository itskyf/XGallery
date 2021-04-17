package com.team02.xgallery.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StoryAdapter(private val dataSet: Array<String>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {


        //return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}
