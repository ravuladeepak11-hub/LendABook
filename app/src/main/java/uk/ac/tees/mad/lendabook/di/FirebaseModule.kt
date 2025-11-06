package uk.ac.tees.mad.lendabook.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideFirebaseAuth(firebaseAuth: FirebaseAuth): FirebaseAuthRepoImpl {
        return FirebaseAuthRepoImpl(firebaseAuth)
    }

}