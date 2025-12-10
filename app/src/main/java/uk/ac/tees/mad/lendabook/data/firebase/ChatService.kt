package uk.ac.tees.mad.lendabook.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.lendabook.data.model.Message

class ChatService(private val firestore: FirebaseFirestore) {
    companion object {
        const val CHATS_COLLECTION = "chats"
        const val MESSAGE_COLLECTION = "message"
        const val PUBLIC_CHAT_ID = "public"
    }
    private val chats = firestore.collection(CHATS_COLLECTION)

    suspend fun sendMessage(message: Message) {
        val chatId = PUBLIC_CHAT_ID
        chats.document(chatId)
            .collection(MESSAGE_COLLECTION)
            .document(message.id)
            .set(message)
            .await()
    }

    fun getPublicMessages(): Flow<List<Message>> = callbackFlow {
        val listener = chats.document(PUBLIC_CHAT_ID)
            .collection(MESSAGE_COLLECTION)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.toObjects(Message::class.java).orEmpty()
                Log.d("TAG", "getPublicMessages: $messages")
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }


}