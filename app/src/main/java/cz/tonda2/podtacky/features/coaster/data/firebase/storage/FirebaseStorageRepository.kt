package cz.tonda2.podtacky.features.coaster.data.firebase.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.UUID

class FirebaseStorageRepository(
    firebaseStorage: FirebaseStorage,
) {
    private val storageRef = firebaseStorage.reference

    /**
     * Uploads image at provided uri to Firebase Storage.
     * Returns path to firebase storage or empty string if upload failed.
     */
    suspend fun uploadPicture(userId: String, uri: Uri): String {
        if (uri == Uri.EMPTY) return ""

        val path = getPath(userId)

        val imgRefFront = storageRef.child(path)
        val uploadTask = imgRefFront.putFile(uri).await()

        return if (uploadTask.task.isSuccessful) path else ""
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

    suspend fun downloadPicture(context: Context, path: String, uri: Uri): Boolean {
        if (path.isEmpty()) return false

        val imgRef = storageRef.child(path)

        val downloadedBytes = imgRef.getBytes(Long.MAX_VALUE).await()
        if (downloadedBytes.isEmpty()) {
            return false
        }

        val bitmap = BitmapFactory.decodeByteArray(downloadedBytes, 0, downloadedBytes.size)
        return try {
            saveImageToUri(context, bitmap, uri)
            true
        } catch (e: IOException) {
            Log.e("STORAGE", "Failed to download $path", e)
            false
        }
    }

    private fun saveImageToUri(context: Context, bitmap: Bitmap, uri: Uri) {
        val contentResolver = context.contentResolver
        val outputStream = contentResolver.openOutputStream(uri)

        if (outputStream != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        }
    }

    private fun getPath(userId: String) = "users/${userId}/coasterImages/${UUID.randomUUID()}"
}