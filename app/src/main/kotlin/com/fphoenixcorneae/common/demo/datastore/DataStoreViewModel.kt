package com.fphoenixcorneae.common.demo.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DataStoreViewModelFactory(
    val userRepository: UserRepository = UserRepository(),
    val settingsRepository: SettingsRepository = SettingsRepository(),
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
    val userRepository: UserRepository,
    val settingsRepository: SettingsRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            userRepository.userId.put("11111")
            userRepository.userNickname.put("一脸懵逼")
            settingsRepository.bindQQSwitch.put(true)
            settingsRepository.bindEmailSwitch.put(false)
        }
    }
}