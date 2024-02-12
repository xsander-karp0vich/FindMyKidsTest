package com.karpovich.findmykidstest.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karpovich.findmykidstest.app.data.AppRepository
import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserDetailsViewModel(private val login: String, private val appRepository: AppRepository) : ViewModel() {

    private val _gitHubUserDetails = MutableLiveData<GitHubUserEntity>()
    val gitHubUserDetails: LiveData<GitHubUserEntity>
        get() = _gitHubUserDetails

    private val _isGitHubUserLoading = MutableLiveData<Boolean>()
    val isGitHubUserLoading: LiveData<Boolean>
        get() = _isGitHubUserLoading

    private val _gitHubFollowers = MutableLiveData<List<GitHubUserEntity>?>()
    val gitHubFollowers: LiveData<List<GitHubUserEntity>?>
        get() = _gitHubFollowers

    private val _errorMessage = MutableLiveData<Boolean>()
    val errorMessage: LiveData<Boolean>
        get() = _errorMessage

    init {
        loadGitHubUserDetails(login)
    }

    private fun loadGitHubUserDetails(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isGitHubUserLoading.postValue(true)

                val userDetailsResponse = appRepository.getGitHubUser(login)
                handleUserDetailsResponse(userDetailsResponse)

                val followersResponse = appRepository.getGitHubFollowers(login, 15)
                handleFollowersResponse(followersResponse)

            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isGitHubUserLoading.postValue(false)
            }
        }
    }

    private fun handleUserDetailsResponse(response: Response<GitHubUserEntity>) {
        if (response.isSuccessful) {
            _gitHubUserDetails.postValue(response.body())
        } else {
            _errorMessage.postValue(true)
        }
    }

    private suspend fun handleFollowersResponse(response: Response<List<GitHubUserEntity>>) {
        if (response.isSuccessful) {
            val followers = response.body()
            followers?.let {
                for (follower in it) {
                    updateUserDetails(follower)
                }
                _gitHubFollowers.postValue(followers)
            }
        } else {
            _errorMessage.postValue(true)
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
                }
            }
        }
    }

    private fun handleError(e: Exception) {
        _errorMessage.postValue(true)
    }
}

