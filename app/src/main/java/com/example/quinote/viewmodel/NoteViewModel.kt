package com.example.quinote.viewmodel

import androidx.lifecycle.*
import com.example.quinote.models.Note
import com.example.quinote.repo.NoteRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(private val repo: NoteRepo) : ViewModel() {

    val allNotes:LiveData<List<Note>> = repo.allNotes.asLiveData()

    fun getAllByUserId(uid: Int) : LiveData<List<Note>> {
        return repo.getAllByUserId(uid).asLiveData()
    }


    fun insert(note: Note) = viewModelScope.launch {
        repo.insert(note)
    }
    fun update(note: Note) = viewModelScope.launch {
        repo.update(note)
    }
    fun delete(note: Note) = viewModelScope.launch {
        repo.delete(note)
    }
}
class NoteViewModelFactory(private val repository: NoteRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}