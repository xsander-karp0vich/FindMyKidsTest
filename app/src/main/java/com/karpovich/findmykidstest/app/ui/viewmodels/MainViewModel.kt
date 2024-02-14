package com.karpovich.findmykidstest.app.ui.viewmodels
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karpovich.findmykidstest.app.data.AppRepository
import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val appRepository: AppRepository) : ViewModel() {

    private val _isGitHubUsersLoading = MutableLiveData<Boolean>()
    val isGitHubUsersLoading: LiveData<Boolean>
        get() = _isGitHubUsersLoading

    private val _gitHubUsers = MutableLiveData<List<GitHubUserEntity>?>()
    val gitHubUsers: LiveData<List<GitHubUserEntity>?>
        get() = _gitHubUsers

    private val _errorMessage = MutableLiveData<Boolean>()
    val errorMessage: LiveData<Boolean>
        get() = _errorMessage
    private var since = 0
    private val allGitHubUsers = mutableListOf<GitHubUserEntity>()

    init {
        loadGitHubUsers()
    }

    fun loadGitHubUsers() {
        _isGitHubUsersLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = appRepository.getGitHubUsers(10, since)

                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        for (user in it) {

                            val existingUser = allGitHubUsers.find { existingUser -> existingUser.id == user.id }

                            if (existingUser == null) {
                                updateUserDetails(user)
                                allGitHubUsers.add(user)
                            } else {
                                updateUserDetails(existingUser)
                            }
                        }

                        _gitHubUsers.postValue(allGitHubUsers.toList())
                        since += it.size
                    } ?: Log.e("MainViewModel", "Empty user list received")
                } else {
                    handleFailedResponse(response)
                }
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isGitHubUsersLoading.postValue(false)
            }
        }
    }

    private suspend fun updateUserDetails(user: GitHubUserEntity) {
        user.login?.let { login ->
            val userResponse = appRepository.getGitHubUser(login)

            if (userResponse.isSuccessful) {
                val userDetails = userResponse.body()
                userDetails?.let {
                    user.followers = it.followers ?: 0
                    user.publicRepos = it.publicRepos ?: 0
                } ?: Log.e("MainViewModel", "Empty user details received for $login")
            } else {
                Log.e("MainViewModel", "Failed to load user details for $login: ${userResponse.message()}")
                Log.e("MainViewModel", "Failed to load user details for $login: ${userResponse.code()}")
            }
        }
    }

    private fun handleFailedResponse(response: Response<List<GitHubUserEntity>>) {
        _errorMessage.postValue(true)
    }

    private fun handleError(e: Exception) {
        _errorMessage.postValue(true)
    }
}
