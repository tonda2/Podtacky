package cz.tonda2.podtacky.features.profile.data

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import cz.tonda2.podtacky.features.profile.domain.User
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

    fun logOut(context: Context) {
        firebaseAuth.signOut()
        AuthUI.getInstance().signOut(context)
    }
}