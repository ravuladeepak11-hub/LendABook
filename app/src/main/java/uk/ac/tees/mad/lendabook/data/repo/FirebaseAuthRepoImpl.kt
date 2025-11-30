package uk.ac.tees.mad.lendabook.data.repo

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.lendabook.domain.model.User
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import javax.inject.Inject

class FirebaseAuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : FirebaseAuthRepo {

    companion object {
        const val USER_COLLECTION = "users"
    }

    override suspend fun signUp(user: User, password: String): Result<Unit> {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()

            result.user?.uid?.let {
                val user = User(
                    id = it,
                    name = user.name,
                    email = user.email,
                )
                firebaseFirestore.collection(USER_COLLECTION)
                    .document(it)
                    .set(user)
                    .await()
            }
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

    override suspend fun getUser(): Result<User?> {
        try {
            val userId = firebaseAuth.currentUser?.uid ?: ""
            val snapDoc = firebaseFirestore.collection(USER_COLLECTION)
                .document(userId)
                .get()
                .await()
            val user = snapDoc.toObject(User::class.java)
            Log.d("TAG", "getUser: $user")
            return Result.success(user)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun checkAuthStatus(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser != null).isSuccess
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }
}
