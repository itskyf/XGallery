package com.team02.xgallery.ui.album.local

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentLocalAlbumListBinding

class LocalAlbumListFragment : Fragment() {
    private lateinit var binding: FragmentLocalAlbumListBinding
    private var spanCount: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLocalAlbumListBinding.inflate(layoutInflater)

        val adapter = LocalAlbumListAdapter()
        binding.gridAlbum.adapter = adapter
        binding.gridAlbum.addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.spacing_medium).toInt(), spanCount))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val manager = GridLayoutManager(activity, spanCount, GridLayoutManager.VERTICAL, false)
        binding.gridAlbum.layoutManager = manager

        return binding.root
    }
}

class MarginItemDecoration(private val spaceHeight: Int, private val spanCount: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            val index = parent.getChildAdapterPosition(view) % spanCount + 1
            if (index == 1) left = 0
            else left = spaceHeight / 2
            if (index == spanCount) right = 0
            else right = spaceHeight / 2
            bottom = spaceHeight
        }
    }
}