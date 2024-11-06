package com.fphoenixcorneae.common.demo.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DataStoreViewModelFactory(
    private val userRepository: UserRepository = UserRepository(),
    private val settingsRepository: SettingsRepository = SettingsRepository(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DataStoreViewModel::class.java)) {
            DataStoreViewModel(userRepository, settingsRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

class DataStoreViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val userId = userRepository.userId.asLiveData()
    val userNickname = userRepository.userNickname.asLiveData()

    val bindQQSwitch = settingsRepository.bindQQSwitch.asLiveData()
    val bindEmailSwitch = settingsRepository.bindEmailSwitch.asLiveData()

    fun setUserId(userId: String?) {
        viewModelScope.launch {
            userRepository.userId.put(userId)
        }
    }

    fun setUserNickname(userNickname: String?) {
        viewModelScope.launch {
            userRepository.userNickname.put(userNickname)
        }
    }

    fun setBindQQSwitch(bindQQSwitch: Boolean?) {
        viewModelScope.launch {
            settingsRepository.bindQQSwitch.put(bindQQSwitch)
        }
    }

    fun setBindEmailSwitch(bindEmailSwitch: Boolean?) {
        viewModelScope.launch {
            settingsRepository.bindEmailSwitch.put(bindEmailSwitch)
        }
    }
}