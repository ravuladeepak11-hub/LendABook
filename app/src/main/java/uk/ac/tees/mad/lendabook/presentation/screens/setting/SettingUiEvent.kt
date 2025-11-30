package uk.ac.tees.mad.lendabook.presentation.screens.setting

sealed class SettingUiEvent {
    object SignOutClick : SettingUiEvent()
    object DeleteAccountClick : SettingUiEvent()
}

enum class SettingNavigation() {
    SIGN_OUT, DELETE_ACCOUNT
}