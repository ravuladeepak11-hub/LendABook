package uk.ac.tees.mad.lendabook.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.data.model.Message
import uk.ac.tees.mad.lendabook.domain.repo.ChatRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _navAction = MutableSharedFlow<ChatNavAction>()
    val navAction = _navAction.asSharedFlow()

    fun onEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.MessageChange ->
                _uiState.update { it.copy(currentMessage = event.value) }
            ChatUiEvent.OnSendChat -> send()
        }
    }

    fun initChat(chatId: String, currentUserId: String, receiverId: String) {
        _uiState.update { it.copy(chatId = chatId, senderId = currentUserId, receiverId = receiverId) }
        viewModelScope.launch {
            chatRepository.getMessages(chatId).collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }

    private fun send() = viewModelScope.launch {
        val state = _uiState.value
        if (state.currentMessage.isBlank()) return@launch
        val msg = Message(
            id = UUID.randomUUID().toString(),
            chatId = state.chatId,
            senderId = state.senderId,
            receiverId = state.receiverId,
            content = state.currentMessage
        )
        chatRepository.sendMessage(state.chatId, msg)
        _uiState.update { it.copy(currentMessage = "") }
    }

}