package cz.cvut.fit.podtacky.features.profile.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import cz.cvut.fit.podtacky.features.profile.domain.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class UserRepository(
    private val firebaseAuth: FirebaseAuth
) {

    val userStream = callbackFlow {
        val listener = AuthStateListener { auth ->
            val user = auth.currentUser?.let { firebaseUser ->
                User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName
                )
            }
            trySend(user)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
    }
}