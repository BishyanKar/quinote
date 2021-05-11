package com.example.quinote.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey
    var id: Int,
    var title: String?,
    var desc: String?,
    var path: String?,
    var uid: Int
)