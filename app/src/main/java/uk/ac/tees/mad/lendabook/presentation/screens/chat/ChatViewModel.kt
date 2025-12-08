package uk.ac.tees.mad.lendabook.presentation.screens.chat

import android.util.Log
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
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val firebaseAuthRepo: FirebaseAuthRepo,
) : ViewModel() {

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

    fun initChat(currentUserId: String, receiverId: String) {
        viewModelScope.launch {
            val userId = firebaseAuthRepo.getUserId() ?: ""
            _uiState.update {
                it.copy(
                    chatId = userId,
                    senderId = currentUserId,
                    receiverId = receiverId
                )
            }
            chatRepository.getMessages(userId).collect { messages ->
                Log.d("TAG", "initChat: $messages")
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