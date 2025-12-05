package uk.ac.tees.mad.lendabook.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.lendabook.data.firebase.ChatService
import uk.ac.tees.mad.lendabook.data.repo.AddBookRepoImpl
import uk.ac.tees.mad.lendabook.data.repo.ChatRepositoryImpl
import uk.ac.tees.mad.lendabook.data.repo.FirebaseAuthRepoImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
    ): FirebaseAuthRepoImpl {
        return FirebaseAuthRepoImpl(firebaseAuth, firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestore(firebaseFirestore: FirebaseFirestore): AddBookRepoImpl {
        return AddBookRepoImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideChatService(firebaseFirestore: FirebaseFirestore) = ChatService(firebaseFirestore)

    @Provides
    @Singleton
    fun provideChatService(chatService: ChatService) = ChatRepositoryImpl(chatService)


}