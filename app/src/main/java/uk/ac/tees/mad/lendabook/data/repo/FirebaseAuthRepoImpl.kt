package uk.ac.tees.mad.lendabook.data.repo

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import javax.inject.Inject

class FirebaseAuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : FirebaseAuthRepo {

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Result<Unit> {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun forgetPassword(email: String): Result<Unit> {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteUser(): Result<Unit> {
        try {
            firebaseAuth.currentUser?.delete()?.await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        try {
            firebaseAuth.signOut()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getUserId(): String? = firebaseAuth.currentUser?.uid

}
