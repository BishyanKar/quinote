package com.example.quinote.repo

import androidx.annotation.WorkerThread
import com.example.quinote.dao.NoteDao
import com.example.quinote.models.Note
import kotlinx.coroutines.flow.Flow

class NoteRepo(private val noteDao: NoteDao){

    val allNotes: Flow<List<Note>> = noteDao.getAll();

    fun getAllByUserId(uid: Int) : Flow<List<Note>> {
        return noteDao.getAllByUserId(uid)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}