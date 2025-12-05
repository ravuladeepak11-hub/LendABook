package uk.ac.tees.mad.lendabook.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.lendabook.data.model.Message

class ChatService(
    private val firestore: FirebaseFirestore
) {

    companion object{
        const val CHATS_COLLECTION = "chats"
        const val MESSAGE_COLLECTION = "message"
    }

    private val chats = firestore.collection(CHATS_COLLECTION)

    suspend fun sendMessage(chatId: String, message: Message) {
        chats.document(chatId)
            .collection(MESSAGE_COLLECTION)
            .document(message.id)
            .set(message)
            .await()
    }

    fun getMessages(chatId: String) = callbackFlow {
        val listener = chats.document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.toObjects(Message::class.java).orEmpty()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }
}