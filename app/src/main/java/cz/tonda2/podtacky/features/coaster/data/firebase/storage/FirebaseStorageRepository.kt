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

    suspend fun downloadPicture(context: Context, path: String, uri: Uri) {
        if (path.isEmpty()) return

        val imgRef = storageRef.child(path)
        imgRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            try {
                saveImageToUri(context, bitmap, uri)
            }
            catch (e: IOException) {
                Log.e("STORAGE", "Failed to download $path", e)
            }
        }.await()
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