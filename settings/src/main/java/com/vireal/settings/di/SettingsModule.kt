package com.vireal.settings.di

import com.vireal.settings.SettingsViewModel
import com.vireal.settings.ui.contract.SettingsRepository
import com.yandex.practicum.middle_homework_5.data.data_store.SettingsRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
  single<SettingsRepository> { SettingsRepositoryImpl(androidApplication()) }
  viewModel { SettingsViewModel(get()) }
}