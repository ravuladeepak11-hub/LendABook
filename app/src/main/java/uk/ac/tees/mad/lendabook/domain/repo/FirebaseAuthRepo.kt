package uk.ac.tees.mad.lendabook.domain.repo

interface FirebaseAuthRepo {

    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun logIn(email: String, password: String): Result<Unit>
    suspend fun forgetPassword(email: String): Result<Unit>
    suspend fun deleteUser(): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun getUserId(): String?

}