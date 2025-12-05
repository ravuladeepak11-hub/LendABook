package uk.ac.tees.mad.lendabook.presentation.screens.chat

import uk.ac.tees.mad.lendabook.data.model.Message

data class ChatUiState(
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val messages: List<Message> = emptyList(),
    val currentMessage: String = ""
)
