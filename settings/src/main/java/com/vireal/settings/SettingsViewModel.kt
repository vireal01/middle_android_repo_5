package com.vireal.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vireal.settings.data_store.SettingContainer
import com.vireal.settings.ui.contract.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
  private val settingsRepository: SettingsRepository
) : ViewModel() {

  fun saveSetting(periodic: Long, delayed: Long) {
    viewModelScope.launch {
      settingsRepository.saveSetting(periodic = periodic, delayed = delayed)
    }
  }

  fun getCurrentSetting(): SettingContainer {
    return settingsRepository.settingData.value
  }
}