package com.example.quinote.repo

import androidx.annotation.WorkerThread
import com.example.quinote.dao.ImageDao
import com.example.quinote.models.Image
import com.example.quinote.models.Note
import kotlinx.coroutines.flow.Flow

class ImageRepo(private val imageDao: ImageDao) {

    val allImages: Flow<List<Image>> = imageDao.getAll();


    fun getAllForNote(noteid: Int): Flow<List<Image>> {
        return imageDao.getAllForNote(noteid)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllForNote(noteid: Int) {
        imageDao.deleteAllForNote(noteid)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(image: Image) {
        imageDao.insert(image)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(image: Image) {
        imageDao.update(image)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(image: Image) {
        imageDao.delete(image)
    }
}