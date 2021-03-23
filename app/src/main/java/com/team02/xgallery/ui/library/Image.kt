package com.team02.xgallery.ui.library

class Image (var _title: String, var _image: Int) {
    fun getImage(): Int {
        return _image
    }

    fun getTitle(): String {
        return _title
    }

}