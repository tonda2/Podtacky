package cz.cvut.fit.podtacky.features.coaster.data.firebase.storage

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FirebaseStorageRepository(
    firebaseStorage: FirebaseStorage,
) {
    private val storageRef = firebaseStorage.reference

    fun uploadPicture(userId: String, uri: Uri): String {
        if (uri == Uri.EMPTY) return ""

        val path = getPath(userId)

        val imgRefFront = storageRef.child(path)
        imgRefFront.putFile(uri)
        return path
    }

    fun deletePicture(path: String) {
        if (path.isEmpty()) return
        val imgRef = storageRef.child(path)
        imgRef.delete().addOnCompleteListener {
            Log.d("STORAGE", "Deleted $path")
        }.addOnFailureListener {
            Log.d("STORAGE", "Failed to delete $path")
        }
    }

    private fun getPath(userId: String) = "users/${userId}/coasterImages/${UUID.randomUUID()}"
}