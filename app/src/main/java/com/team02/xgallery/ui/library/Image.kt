package com.team02.xgallery.ui.library

class Image (var _title: String, var _image: Int) {
    fun getImage(): Int {
        return _image
    }

    fun getTitle(): String {
        return _title
    }

    fun setImage(_image: Int) {
        this._image = _image
    }

    fun setTitle(_title: String) {
        this._title = _title
    }
}