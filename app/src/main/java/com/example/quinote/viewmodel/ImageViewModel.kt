package com.example.quinote.viewmodel

import androidx.lifecycle.*
import com.example.quinote.models.Image
import com.example.quinote.repo.ImageRepo
import kotlinx.coroutines.launch

class ImageViewModel(private val imageRepo: ImageRepo) : ViewModel() {

    val allImages: LiveData<List<Image>> = imageRepo.allImages.asLiveData()

    fun getAllForNote(noteid: Int): LiveData<List<Image>> {

        return imageRepo.getAllForNote(noteid).asLiveData()
    }

    fun deleteAllForNote(noteid: Int) = viewModelScope.launch {
        imageRepo.deleteAllForNote(noteid)
    }

    fun insert(image: Image) = viewModelScope.launch {
        imageRepo.insert(image)
    }
    fun update(image: Image) = viewModelScope.launch {
        imageRepo.update(image)
    }
    fun delete(image: Image) = viewModelScope.launch {
        imageRepo.delete(image)
    }
}
class ImageViewModelFactory(private val repository: ImageRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}