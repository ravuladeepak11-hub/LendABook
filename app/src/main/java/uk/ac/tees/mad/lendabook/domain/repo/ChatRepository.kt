package uk.ac.tees.mad.lendabook.domain.repo

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.lendabook.data.model.Message

interface ChatRepository {
    suspend fun sendMessage(chatId: String, message: Message)
    fun getMessages(chatId: String): Flow<List<Message>>
}