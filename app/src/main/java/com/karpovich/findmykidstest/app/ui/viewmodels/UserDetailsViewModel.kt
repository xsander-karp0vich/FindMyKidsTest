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

class UserDetailsViewModel(private val login: String, private val appRepository: AppRepository) : ViewModel() {

    private val TAG = "UserDetailsViewModelTAG"

    // LiveData для отслеживания деталей пользователя
    private val _gitHubUserDetails = MutableLiveData<GitHubUserEntity?>()
    val gitHubUserDetails: LiveData<GitHubUserEntity?>
        get() = _gitHubUserDetails

    // LiveData для отслеживания состояния загрузки деталей пользователя
    private val _isGitHubUserDetailsLoading = MutableLiveData<Boolean>()
    val isGitHubUserDetailsLoading: LiveData<Boolean>
        get() = _isGitHubUserDetailsLoading

    // LiveData для отслеживания состояния загрузки подписчиков пользователя
    private val _isGitHubUserFollowersLoading = MutableLiveData<Boolean>()
    val isGitHubUserFollowersLoading: LiveData<Boolean>
        get() = _isGitHubUserFollowersLoading

    // LiveData для списка подписчиков пользователя
    private val _gitHubFollowers = MutableLiveData<List<GitHubUserEntity>?>()
    val gitHubFollowers: LiveData<List<GitHubUserEntity>?>
        get() = _gitHubFollowers

    // LiveData для отслеживания ошибок
    private val _errorMessage = MutableLiveData<Boolean>()
    val errorMessage: LiveData<Boolean>
        get() = _errorMessage

    // Переменные для управления пагинацией при загрузке подписчиков
    private var perPage = 10
    private val allGitHubFollowers = mutableListOf<GitHubUserEntity>()

    // Кэш для хранения уже загруженных пользователей
    private val userCache = mutableMapOf<String, GitHubUserEntity>()

    init {
        // Загружаем детали пользователя при создании ViewModel
        loadGitHubUserDetails(login)
    }

    private fun loadGitHubUserDetails(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isGitHubUserDetailsLoading.postValue(true)

                // Проверяем, есть ли данные о пользователе в кэше
                val cachedUser = userCache[login]
                if (cachedUser != null) {
                    _gitHubUserDetails.postValue(cachedUser)
                    _isGitHubUserDetailsLoading.postValue(false)
                    Log.d(TAG, "GitHub user details loaded from cache for $login")
                } else {
                    // Отпрвляем запрос на получение деталей пользователя
                    val userDetailsResponse = appRepository.getGitHubUser(login)
                    handleUserDetailsResponse(userDetailsResponse)

                    if (userDetailsResponse.isSuccessful) {
                        // Если детали пользователя успешно загружены, загружаем подписчиков
                        loadGitHubUserFollowers(login)
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun loadGitHubUserFollowers(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isGitHubUserFollowersLoading.postValue(true)
                // Загружаем подписчиков пользователя
                val followersResponse = appRepository.getGitHubFollowers(login, perPage)
                handleFollowersResponse(followersResponse)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleUserDetailsResponse(response: Response<GitHubUserEntity>) {
        if (response.isSuccessful) {
            response.body()?.let {
                _gitHubUserDetails.postValue(it)
                _isGitHubUserDetailsLoading.postValue(false)
                Log.d(TAG, "GitHub user details loaded successfully for ${it.login}")
                // Сохраняем данные о пользователе в кэш
                it.let { userCache[it.login ?: ""] = it }
            }
        } else {
            _errorMessage.postValue(true)
            Log.e(TAG, "Failed to load GitHub user details: ${response.message()}")
            Log.e(TAG, "Failed to load GitHub user details code: ${response.code()}")
        }
    }

    private suspend fun handleFollowersResponse(response: Response<List<GitHubUserEntity>>) {
        if (response.isSuccessful) {
            val followers = response.body()
            followers?.let {
                for (follower in it) {
                    val existingFollower = allGitHubFollowers.find { existingFollower -> existingFollower.id == follower.id }

                    if (existingFollower == null) {
                        updateUserDetails(follower)
                        allGitHubFollowers.add(follower)
                        Log.d(TAG, "GitHub follower details added successfully")
                    } else {
                        updateUserDetails(existingFollower)
                        Log.d(TAG, "GitHub follower details updated successfully")
                    }
                }

                _gitHubFollowers.postValue(allGitHubFollowers.toList())
                perPage += it.size
            }
        } else {
            _errorMessage.postValue(true)
            Log.e(TAG, "Failed to load GitHub followers: ${response.message()}")
            Log.e(TAG, "Failed to load GitHub followers code: ${response.code()}")
        }
    }

    private suspend fun updateUserDetails(user: GitHubUserEntity) {
        user.login?.let { login ->
            // Проверяем, есть ли данные о пользователе в кэше
            if (!userCache.containsKey(login)) {
                // Если данных о пользователе нет в кэше, загружаем их из репозитория
                val userResponse = appRepository.getGitHubUser(login)

                if (userResponse.isSuccessful) {
                    val userDetails = userResponse.body()
                    userDetails?.let {
                        user.followers = it.followers ?: 0
                        user.publicRepos = it.publicRepos ?: 0
                        Log.d(TAG, "GitHub user details updated successfully for $login")
                    } ?: Log.e(TAG, "Empty user details received for $login")
                } else {
                    Log.e(TAG, "Failed to load user details for $login: ${userResponse.message()}")
                    Log.e(TAG, "Failed to load user details for $login: ${userResponse.code()}")
                }

                // Сохраняем данные о пользователе в кэш
                userCache[login] = user
            } else {
                Log.d(TAG, "User details already loaded for $login")
            }
        }
    }

    private fun handleError(e: Exception) {
        _errorMessage.postValue(true)
        Log.e(TAG, "Error occurred: ${e.message}")
    }
}