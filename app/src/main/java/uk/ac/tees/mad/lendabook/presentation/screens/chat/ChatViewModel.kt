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

    init {
        // Get current user ID when ViewModel is created
        viewModelScope.launch {
            val currentUserId = firebaseAuthRepo.getUserId() // or however you get it
            _uiState.update { it.copy(currentUserId = currentUserId ?: "") }
        }
    }

    fun onEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.MessageChange ->
                _uiState.update { it.copy(currentMessage = event.value) }
            ChatUiEvent.OnSendChat -> send()
        }
    }

    fun initPublicChat() {
        viewModelScope.launch {
            chatRepository.getPublicMessages().collect { messages ->
                Log.d("TAG", "initPublicChat: $messages")
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }

    private fun send() = viewModelScope.launch {
        val state = _uiState.value
        if (state.currentMessage.isBlank()) return@launch

        val currentUserId = state.currentUserId.ifEmpty {
            firebaseAuthRepo.getUserId() ?: "anonymous"
        }

        val msg = Message(
            id = UUID.randomUUID().toString(),
            senderId = currentUserId,  // ← ADD THIS
            content = state.currentMessage,
            timestamp = System.currentTimeMillis()
        )
        chatRepository.sendPublicMessage(msg)
        _uiState.update { it.copy(currentMessage = "") }
    }
}