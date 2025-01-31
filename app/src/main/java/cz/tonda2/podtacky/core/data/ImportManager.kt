
package cz.tonda2.podtacky.core.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.storage.StorageException
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.data.db.DbCoaster
import cz.tonda2.podtacky.features.coaster.data.firebase.firestore.FirestoreRepository
import cz.tonda2.podtacky.features.coaster.data.firebase.storage.FirebaseStorageRepository
import cz.tonda2.podtacky.features.coaster.data.toDomain
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ImportManager(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
    private val coasterRepository: CoasterRepository
) {

    suspend fun importBackup(context: Context, onFileDownload: () -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val backedData: Flow<List<DbCoaster>>
        try {
            backedData = firestoreRepository.getCoasters(userId)
        }
        catch (e: Exception) {
            Log.e("IMPORT", "Couldn't get coasters from firestore for user id: $userId!", e)
            Firebase.crashlytics.recordException(e)
            return
        }

        withContext(Dispatchers.IO) {
            backedData.first().forEach { dbCoaster ->
                try {
                    importCoaster(dbCoaster, context)
                    onFileDownload()
                }
                catch (e: StorageException) {
                    Log.e("IMPORT", "Failed to import coaster uid: ${dbCoaster.uid} for user id: $userId", e)
                    Firebase.crashlytics.recordException(e)
                }
            }
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
            val downloaded = firebaseStorageRepository.downloadPicture(context, coaster.frontUri, frontUri)
            if (!downloaded) return
        }
        if (coaster.backUri.isNotEmpty()) {
            backUri = createImageFile(context)
            val downloaded = firebaseStorageRepository.downloadPicture(context, coaster.backUri, backUri)
            if (!downloaded) return
        }

        val newCoaster = Coaster(
            uid = coaster.uid,
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