package com.example.quinote.dao

import androidx.room.*
import com.example.quinote.models.Image
import com.example.quinote.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM image")
    fun getAll(): Flow<List<Image>>

    @Query("SELECT * FROM image WHERE noteID = :noteid")
    fun getAllForNote(noteid : Int): Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: Image)

    @Update
    suspend fun update(image: Image)

    @Delete
    suspend fun delete(image: Image)

    @Query("DELETE FROM image WHERE noteID = :noteid")
    suspend fun deleteAllForNote(noteid : Int)

}