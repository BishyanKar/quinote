package com.example.quinote.viewmodel

import androidx.lifecycle.*
import com.example.quinote.models.User
import com.example.quinote.repo.UserRepo
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepo) : ViewModel() {

    val allUsers: LiveData<List<User>> = userRepo.allUsers.asLiveData()


    fun getUserById(uid: Int): LiveData<User> {
        return userRepo.getUserById(uid).asLiveData()
    }

    fun getUserByEmail(email: String?): LiveData<User> {
        return userRepo.getUserByEmail(email).asLiveData()
    }

    fun insert(user: User) = viewModelScope.launch {
        userRepo.insert(user)
    }
    fun update(user: User) = viewModelScope.launch {
        userRepo.update(user)
    }
    fun delete(user: User) = viewModelScope.launch {
        userRepo.delete(user)
    }
}
class UserViewModelFactory(private val repository: UserRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}