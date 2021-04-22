package com.team02.xgallery.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(marginF: Float, private val spanCount: Int) :
    RecyclerView.ItemDecoration() {
    private val margin = marginF.toInt()
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        when (position % spanCount) {
            0 -> {
                outRect.left = 0
                outRect.right = margin / 2
            }
            1 -> {
                outRect.right = 0
                outRect.left = margin / 2
            }
            else -> {
                outRect.left = margin / 2
                outRect.right = margin / 2
            }
        }
        outRect.top = margin / 2
        outRect.bottom = margin / 2

//        if (position and 1 == 1) {
//            outRect.left = margin shr 1
//            outRect.right = margin
//        } else {
//            outRect.left = margin
//            outRect.right = margin shr 1
//        }
//        if (position < 2) {
//            outRect.top = margin
//        }
    }
}