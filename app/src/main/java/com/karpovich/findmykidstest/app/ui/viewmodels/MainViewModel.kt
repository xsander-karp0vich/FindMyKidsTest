package com.karpovich.findmykidstest.app.ui.viewmodels
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karpovich.findmykidstest.app.data.AppRepository
import com.karpovich.findmykidstest.app.data.network.entities.GitHubItemEntity
import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel: ViewModel() {
    private val _isGitHubUsersLoading = MutableLiveData<Boolean>()
    val isGitHubUsersLoading: LiveData<Boolean>
        get() = _isGitHubUsersLoading

    private val _gitHubUsers = MutableLiveData<List<GitHubItemEntity>?>()
    val gitHubUsers: LiveData<List<GitHubItemEntity>?>
        get() = _gitHubUsers
    init {
        loadGitHubUsers()
    }
    private fun loadGitHubUsers() {
        _isGitHubUsersLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<List<GitHubItemEntity>> = AppRepository.getGitHubUsers(10)

                if (response.isSuccessful) {
                    val users = response.body()
                    _gitHubUsers.postValue(users)
                } else {
                    Log.e("MainViewModel", "Failed to load GitHub users: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading GitHub users", e)
            } finally {
                _isGitHubUsersLoading.postValue(false)
            }
        }
    }
}