package com.vireal.settings.ui.contract

import com.vireal.settings.data_store.SettingContainer
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
  val settingData: StateFlow<SettingContainer>
  suspend fun saveSetting(periodic: Long, delayed: Long)
  suspend fun readSetting()
}