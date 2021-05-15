package com.team02.xgallery.data.entity

import com.google.firebase.Timestamp

data class User(
        val displayName: String,
        val email: String,
        val avatarPath: String,
        val lastSeen: Timestamp = Timestamp.now(),
        val memories1: MutableList<String>? = mutableListOf(),
        val memories2: MutableList<String>? = mutableListOf(),
        val memories3: MutableList<String>? = mutableListOf(),
        val memories4: MutableList<String>? = mutableListOf(),
        val memories5: MutableList<String>? = mutableListOf()
)
