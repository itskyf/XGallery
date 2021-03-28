package com.team02.xgallery

import android.view.View
import android.view.ViewGroup

class Utils {
    companion object {
        fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
            view.isEnabled = enabled
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val child: View = view.getChildAt(i)
                    setViewAndChildrenEnabled(child, enabled)
                }
            }
        }
    }
}