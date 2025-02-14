
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
import cz.tonda2.podtacky.features.folder.data.FolderRepository
import cz.tonda2.podtacky.features.folder.domain.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ImportManager(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
    private val coasterRepository: CoasterRepository,
    private val folderRepository: FolderRepository
) {

    suspend fun importBackup(context: Context, onCoasterDownload: () -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        importFolderBackup(userId)
        importCoasterBackup(userId, context, onCoasterDownload)
    }

    private suspend fun importCoasterBackup(userId: String, context: Context, onCoasterDownload: () -> Unit) {
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
                    onCoasterDownload()
                }
                catch (e: StorageException) {
                    Log.e("IMPORT", "Failed to import coaster uid: ${dbCoaster.uid} for user id: $userId", e)
                    Firebase.crashlytics.recordException(e)
                }
            }
        }
    }

    private suspend fun importFolderBackup(userId: String) {
        val folderBackup: List<Folder>
        try {
            val rawBackup = firestoreRepository.getFolders(userId)
            folderBackup = sortFoldersForImport(rawBackup.first())
        }
        catch (e: Exception) {
            Log.e("IMPORT", "Couldn't get folders from firestore for user id: $userId!", e)
            Firebase.crashlytics.recordException(e)
            return
        }

        withContext(Dispatchers.IO) {
            folderBackup.forEach { folder ->
                importFolder(folder)
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
            folderUid = coaster.folderUid,
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

    private suspend fun importFolder(folder: Folder) {
        if (folderRepository.getFolderByUid(folder.folderUid) == null) {
            folderRepository.addFolder(folder)
        }
        else {
            folderRepository.updateFolder(folder)
        }
    }

    /**
     * Sorts folders in a way so each folder will only come after all it's parents.
     * If imported in this order, no FK will be violated.
     */
    private fun sortFoldersForImport(folders: List<Folder>): List<Folder> {
        val folderMap = folders.associateBy { it.folderUid }
        val sortedFolders = mutableListOf<Folder>()
        val visited = mutableSetOf<String>()

        fun visit(folder: Folder) {
            if (visited.contains(folder.folderUid)) return

            // If folder has a parent, it needs to be imported first to not violate FK -> recursively add all parents
            folder.parentUid?.let { parentId ->
                folderMap[parentId]?.let { visit(it) }
            }

            sortedFolders.add(folder)
            visited.add(folder.folderUid)
        }

        folders.forEach { visit(it) }
        return sortedFolders
    }
}