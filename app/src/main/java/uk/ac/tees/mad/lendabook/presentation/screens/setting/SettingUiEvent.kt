package uk.ac.tees.mad.lendabook.presentation.screens.setting

sealed class SettingUiEvent {
    data class ToggleNotifications(val enabled: Boolean): SettingUiEvent()
    data class ToggleDarkMode(val enabled: Boolean): SettingUiEvent()
    object SignOutClick : SettingUiEvent()
    object DeleteAccountClick : SettingUiEvent()
}

enum class SettingNavigation() {
    SIGN_OUT, DELETE_ACCOUNT
}