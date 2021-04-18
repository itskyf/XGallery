package com.team02.xgallery.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.team02.xgallery.R
import com.team02.xgallery.databinding.ListItemStoryBinding
import com.team02.xgallery.ui.story.StoryFragment


class StoryAdapter(private val dataSet: List<Int>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ListItemStoryBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = dataSet[position]
        holder.bind(img)
    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(val binding: ListItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var navController: NavController
        fun bind(img: Int) {
            binding.storyThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.storyThumbnail.load(img)
            binding.storyThumbnail.setOnClickListener{
                navController.navigate(R.id.story)
            }
        }
    }
}
