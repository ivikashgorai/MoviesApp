package com.example.moviesapp



import androidx.lifecycle.ViewModelProvider
import com.example.moviesapp.ViewModel


class ViewModalFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            return ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
