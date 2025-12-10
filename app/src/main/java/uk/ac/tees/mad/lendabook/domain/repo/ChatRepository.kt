package uk.ac.tees.mad.lendabook.domain.repo

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.lendabook.data.model.Message

interface ChatRepository {
    suspend fun sendPublicMessage(message: Message)
    fun getPublicMessages(): Flow<List<Message>>
}