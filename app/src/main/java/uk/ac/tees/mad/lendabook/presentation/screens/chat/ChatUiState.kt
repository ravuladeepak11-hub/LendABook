package uk.ac.tees.mad.lendabook.presentation.screens.chat

import uk.ac.tees.mad.lendabook.data.model.Message

data class ChatUiState(
    val currentUserId: String = "",  // NEW: For dynamic UID checks and display
    val messages: List<Message> = emptyList(),
    val currentMessage: String = ""
)