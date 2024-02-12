package com.karpovich.findmykidstest.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karpovich.findmykidstest.app.data.AppRepository

class UserDetailsViewModelFactory(private val login: String,private val appRepository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailsViewModel::class.java)) {
            return UserDetailsViewModel(login, appRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}