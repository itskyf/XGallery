package com.team02.xgallery.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AlbumItemDecoration(marginF: Float) : RecyclerView.ItemDecoration() {
    private val margin = marginF.toInt()
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        if (position and 1 == 1) {
            outRect.left = margin shr 1
            outRect.right = margin
        } else {
            outRect.left = margin
            outRect.right = margin shr 1
        }
        if (position < 2) {
            outRect.top = margin
        }
    }
}