package com.example.quinote.repo

import androidx.annotation.WorkerThread
import com.example.quinote.dao.UserDao
import com.example.quinote.models.User
import kotlinx.coroutines.flow.Flow

class UserRepo(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAll();

    fun getUserByEmail(email: String?): Flow<User>{
        return userDao.getUserByEmail(email)
    }
    fun getUserById(uid: Int): Flow<User> {
        return userDao.getUserById(uid)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(user: User) {
        userDao.update(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(user: User) {
        userDao.delete(user)
    }
}