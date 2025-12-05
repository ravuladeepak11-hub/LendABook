package uk.ac.tees.mad.lendabook.data.repo

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.lendabook.data.firebase.ChatService
import uk.ac.tees.mad.lendabook.data.model.Message
import uk.ac.tees.mad.lendabook.domain.repo.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService,
) : ChatRepository {

    override suspend fun sendMessage(chatId: String, message: Message) =
        chatService.sendMessage(chatId, message)

    override fun getMessages(chatId: String): Flow<List<Message>> =
        chatService.getMessages(chatId)
}