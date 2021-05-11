package com.example.quinote.dao

import androidx.room.*
import com.example.quinote.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id = :uid")
    fun getUserById(uid: Int): Flow<User>

    @Query("SELECT * FROM user WHERE email =:email")
    fun getUserByEmail(email: String?): Flow<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

}