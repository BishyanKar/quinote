package com.example.quinote.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val email: String?,
    val password: String?,
    val mobile: String?
){

    @PrimaryKey
    var id: Int = 0
}