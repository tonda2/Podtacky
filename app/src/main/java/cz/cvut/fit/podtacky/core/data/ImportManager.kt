
package cz.cvut.fit.podtacky.core.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.data.db.DbCoaster
import cz.cvut.fit.podtacky.features.coaster.data.db.toDomain
import cz.cvut.fit.podtacky.features.coaster.data.firebase.firestore.FirestoreRepository
import cz.cvut.fit.podtacky.features.coaster.data.firebase.storage.FirebaseStorageRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ImportManager(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
    private val coasterRepository: CoasterRepository
) {

    suspend fun importBackup(context: Context, onFileDownload: () -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        try {
            val backupedData = firestoreRepository.getCoasters(userId)
            withContext(Dispatchers.IO) {
                backupedData.first().forEach { dbCoaster ->
                    importCoaster(dbCoaster, context)
                    onFileDownload()
                }
            }
        }
        catch (e: Exception) {
            Log.e("IMPORT", "Exception thrown when importing!", e)
            return
        }
    }

    private suspend fun importCoaster(coaster: DbCoaster, context: Context) {
        if (coasterRepository.isCoasterDuplicate(coaster.toDomain())) {
            return
        }

        var frontUri = Uri.EMPTY
        var backUri = Uri.EMPTY

        if (coaster.frontUri.isNotEmpty()) {
            frontUri = createImageFile(context)
            firebaseStorageRepository.downloadPicture(context, coaster.frontUri, frontUri)
        }
        if (coaster.backUri.isNotEmpty()) {
            backUri = createImageFile(context)
            firebaseStorageRepository.downloadPicture(context, coaster.backUri, backUri)
        }

        val newCoaster = Coaster(
            brewery = coaster.brewery,
            description = coaster.description,
            dateAdded = coaster.dateAdded,
            city = coaster.city,
            count = coaster.count,
            frontUri = frontUri,
            backUri = backUri,
            uploaded = true,
            deleted = false
        )

        coasterRepository.addCoaster(newCoaster)
    }
}