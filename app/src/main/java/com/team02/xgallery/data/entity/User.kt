package com.team02.xgallery.data.entity

import com.google.firebase.Timestamp

data class User(
        val displayName: String,
        val email: String,
        val avatarPath: String,
        val lastSeen: Timestamp = Timestamp.now(),
        val memories: MutableList<String>? = mutableListOf<String>()
)
