package uk.ac.tees.mad.lendabook.domain.repo

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.lendabook.domain.model.User

interface FirebaseAuthRepo {

    suspend fun signUp(user: User, password: String): Result<Unit>
    suspend fun logIn(email: String, password: String): Result<Unit>
    suspend fun forgetPassword(email: String): Result<Unit>
    suspend fun deleteUser(): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun getUserId(): String?
    suspend fun getUser(): Result<User?>
    fun checkAuthStatus(): Flow<Boolean>

}