package uk.ac.tees.mad.lendabook.presentation.screens.chat

sealed class ChatUiEvent {
    data class MessageChange(val value: String) : ChatUiEvent()
    object OnSendChat : ChatUiEvent()
}

sealed class ChatNavAction {
    object Back : ChatNavAction()
}