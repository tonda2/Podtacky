package cz.tonda2.podtacky.features.coaster.data.firebase.firestore

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import cz.tonda2.podtacky.features.coaster.data.db.DbCoaster
import cz.tonda2.podtacky.features.coaster.data.firebase.storage.FirebaseStorageRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.folder.domain.Folder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseStorageRepository: FirebaseStorageRepository
) {

    companion object {
        const val MAIN_COLLECTION = "users"
        const val COASTER_COLLECTION = "coasters"
        const val FOLDER_COLLECTION = "folders"
    }

    // Hazi vyjimku pokud neni internet!!!
    fun getCoasters(userId: String): Flow<List<DbCoaster>> = flow {
        val collection = firestore.collection(MAIN_COLLECTION)
            .document(userId)
            .collection(COASTER_COLLECTION)

        val query = collection.get(Source.SERVER).await()
        emit(query.toObjects(DbCoaster::class.java))
    }

    fun getFolders(userId: String): Flow<List<Folder>> = flow {
        val collection = firestore.collection(MAIN_COLLECTION)
            .document(userId)
            .collection(FOLDER_COLLECTION)

        val query = collection.get(Source.SERVER).await()
        emit(query.toObjects(Folder::class.java))
    }

    suspend fun addCoaster(userId: String, coaster: Coaster): Boolean {
        try {
            firestore.collection(MAIN_COLLECTION)
                .document(userId)
                .collection(COASTER_COLLECTION)
                .document(coaster.uid)
                .set(coaster)
                .await()
            return true
        }
        catch (e: Exception) {
            Firebase.crashlytics.recordException(RuntimeException("Couldn't upload coaster $coaster to Firestore!"))
            return false
        }
    }

    fun deleteCoaster(userId: String, uid: String) {
        firestore.collection(MAIN_COLLECTION)
            .document(userId)
            .collection(COASTER_COLLECTION)
            .document(uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doc = task.result

                    firebaseStorageRepository.deletePicture(doc.get("frontUri").toString())
                    firebaseStorageRepository.deletePicture(doc.get("backUri").toString())

                    doc.reference.delete().addOnSuccessListener {
                        Log.d("FS DELETE", "Deleted ${doc.id}")
                    }
                }
                else {
                    Log.d("FS DELETE", "Error deleting: ", task.exception)
                }
            }
    }

    suspend fun addFolder(userId: String, folder: Folder): Boolean {
        try {
            firestore.collection(MAIN_COLLECTION)
                .document(userId)
                .collection(FOLDER_COLLECTION)
                .document(folder.folderUid)
                .set(folder)
                .await()
            return true
        }
        catch (e: Exception) {
            Firebase.crashlytics.recordException(RuntimeException("Couldn't upload folder $folder to Firestore!"))
            return false
        }
    }

    fun deleteFolder(userId: String, uid: String) {
        firestore.collection(MAIN_COLLECTION)
            .document(userId)
            .collection(FOLDER_COLLECTION)
            .document(uid)
            .delete()
            .addOnSuccessListener { Log.d("FS DELETE", "Folder $uid deleted from FS!") }
            .addOnFailureListener { e -> Log.w("FS DELETE", "Error deleting folder $uid from FS", e) }
    }
}