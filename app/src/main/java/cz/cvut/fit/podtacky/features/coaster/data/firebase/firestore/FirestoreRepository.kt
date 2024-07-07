package cz.cvut.fit.podtacky.features.coaster.data.firebase.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import cz.cvut.fit.podtacky.features.coaster.data.db.DbCoaster
import cz.cvut.fit.podtacky.features.coaster.data.firebase.storage.FirebaseStorageRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseStorageRepository: FirebaseStorageRepository
) {

    // Hazi vyjimku pokud neni internet!!!
    fun getCoasters(userId: String): Flow<List<DbCoaster>> = flow {
        val collection = firestore.collection("users")
            .document(userId)
            .collection("coasters")

        val query = collection.get(Source.SERVER).await()
        emit(query.toObjects(DbCoaster::class.java))
    }

    fun addCoaster(userId: String, coaster: Coaster) {
        firestore.collection("users")
            .document(userId)
            .collection("coasters")
            .add(coaster)
    }

    fun deleteCoaster(userId: String, coasterId: String) {
        val collection = firestore.collection("users")
            .document(userId)
            .collection("coasters")
        val query = collection.whereEqualTo("coasterId", coasterId.toInt())

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result!!) {
                    firebaseStorageRepository.deletePicture(doc.get("frontUri").toString())
                    firebaseStorageRepository.deletePicture(doc.get("backUri").toString())

                    collection.document(doc.id).delete()
                    Log.d("FS DELETE", "Deleted ${doc.id}")
                }
            }
            else {
                Log.d("FS DELETE", "Error deleting: ", task.exception)
            }
        }
    }
}