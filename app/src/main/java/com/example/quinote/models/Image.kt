package com.example.quinote.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    var path: String?,
    var noteID : Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
