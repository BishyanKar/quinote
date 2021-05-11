package com.example.quinote

import android.app.Application
import com.example.quinote.database.NoteDatabase
import com.example.quinote.repo.ImageRepo
import com.example.quinote.repo.NoteRepo
import com.example.quinote.repo.UserRepo

class NoteApp: Application() {
    val database by lazy { NoteDatabase.getDatabase(this) }
    val noteRepository by lazy { NoteRepo(database.noteDao()) }
    val userRepository by lazy { UserRepo(database.userDao()) }
    val imageRepository by lazy { ImageRepo(database.imageDao()) }
}