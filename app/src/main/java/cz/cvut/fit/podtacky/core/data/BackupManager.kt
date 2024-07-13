package cz.cvut.fit.podtacky.core.data

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.data.firebase.firestore.FirestoreRepository
import cz.cvut.fit.podtacky.features.coaster.data.firebase.storage.FirebaseStorageRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.first

class BackupManager(
    private val coasterRepository: CoasterRepository,
    private val firestoreRepository: FirestoreRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository
) {

    suspend fun createBackup() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val coasters = coasterRepository.getCoastersFlow().first()
        val toDelete = coasters.filter { c -> c.deleted }
        val toUpload = coasters.filter { c -> !c.deleted && !c.uploaded }

        var count = 0

        toUpload.forEach { coaster ->
            if (uploadCoaster(userId, coaster)) {
                coasterRepository.markUploaded(coaster.coasterId.toString())
                count += 1
            }
        }

        toDelete.forEach { coaster ->
            if (deleteCoaster(userId, coaster)) {
                count += 1
            }
        }

        Log.d("BACKUP", "$count/${toDelete.size + toUpload.size} coasters backed up!")
    }

    private fun uploadCoaster(userId: String, coaster: Coaster): Boolean {
        val frontPath = firebaseStorageRepository.uploadPicture(userId, coaster.frontUri)
        val backPath = firebaseStorageRepository.uploadPicture(userId, coaster.backUri)

        firestoreRepository.addCoaster(userId, Coaster(
            uid = coaster.uid,
            coasterId = coaster.coasterId,
            brewery = coaster.brewery,
            description = coaster.description,
            dateAdded = coaster.dateAdded,
            city = coaster.city,
            count = coaster.count,
            frontUri = Uri.parse(frontPath),
            backUri = Uri.parse(backPath),
            uploaded = true,
            deleted = coaster.deleted
        ))

        return true
    }

    private suspend fun deleteCoaster(userId: String, coaster: Coaster): Boolean{
        firestoreRepository.deleteCoaster(userId, coaster.uid)
        coasterRepository.deleteCoaster(coaster)
        return true
    }
}