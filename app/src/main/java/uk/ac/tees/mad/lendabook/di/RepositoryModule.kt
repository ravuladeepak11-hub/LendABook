package uk.ac.tees.mad.lendabook.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.lendabook.data.repo.AddBookRepoImpl
import uk.ac.tees.mad.lendabook.data.repo.ApiBookRepoImpl
import uk.ac.tees.mad.lendabook.data.repo.ChatRepositoryImpl
import uk.ac.tees.mad.lendabook.data.repo.FirebaseAuthRepoImpl
import uk.ac.tees.mad.lendabook.domain.repo.AddBookRepo
import uk.ac.tees.mad.lendabook.domain.repo.ApiBookRepo
import uk.ac.tees.mad.lendabook.domain.repo.ChatRepository
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideFirebaseAuthRepo(firebaseAuthRepoImpl: FirebaseAuthRepoImpl): FirebaseAuthRepo

    @Binds
    @Singleton
    fun provideFirestoreRepo(addBookRepoImpl: AddBookRepoImpl): AddBookRepo

    @Binds
    @Singleton
    fun provideApiBookRepo(apiBookRepoImpl: ApiBookRepoImpl): ApiBookRepo


    @Binds
    @Singleton
    fun provideChatRepo(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository
}