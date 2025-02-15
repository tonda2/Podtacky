package cz.tonda2.podtacky.core.data

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.crashlytics
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.data.firebase.firestore.FirestoreRepository
import cz.tonda2.podtacky.features.coaster.data.firebase.storage.FirebaseStorageRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.folder.data.FolderRepository
import cz.tonda2.podtacky.features.folder.domain.Folder
import kotlinx.coroutines.flow.first

class BackupManager(
    private val coasterRepository: CoasterRepository,
    private val folderRepository: FolderRepository,
    private val firestoreRepository: FirestoreRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository
) {

    suspend fun createBackup() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        createCoasterBackup(userId)
        createFolderBackup(userId)
    }

    private suspend fun createCoasterBackup(userId: String) {
        val coasters = coasterRepository.getAllCoasters().first()
        val toDelete = coasters.filter { c -> c.deleted }
        val toUpload = coasters.filter { c -> !c.deleted && !c.uploaded }

        var count = 0

        toUpload.forEach { coaster ->
            if (uploadCoaster(userId, coaster)) {
                coasterRepository.markUploaded(coaster.coasterId.toString())
                count += 1
            }
            else {
                Firebase.crashlytics.recordException(RuntimeException("Failed to upload coaster ${coaster} to Firestore"))
            }
        }

        toDelete.forEach { coaster ->
            if (deleteCoaster(userId, coaster)) {
                count += 1
            }
        }

        Log.d("BACKUP", "$count/${toDelete.size + toUpload.size} coasters backed up!")
    }

    private suspend fun createFolderBackup(userId: String) {
        val folders = folderRepository.getAllFolders().first()
        val toDelete = folders.filter { f -> f.deleted }
        val toUpload = folders.filter { f -> !f.deleted && !f.uploaded }

        toUpload.forEach { folder ->
            if (uploadFolder(userId, folder)) {
                folderRepository.markUploaded(folder.folderUid)
            }
            else {
                Firebase.crashlytics.recordException(RuntimeException("Failed to upload folder $folder to Firestore"))
            }
        }

        toDelete.forEach { folder ->
            deleteFolder(userId, folder)
        }
    }

    private suspend fun uploadCoaster(userId: String, coaster: Coaster): Boolean {
        val frontPath = firebaseStorageRepository.uploadPicture(userId, coaster.frontUri)
        if (coaster.frontUri != Uri.EMPTY && frontPath.isEmpty()) return false

        val backPath = firebaseStorageRepository.uploadPicture(userId, coaster.backUri)
        if (coaster.backUri != Uri.EMPTY && backPath.isEmpty()) {
            if (coaster.frontUri != Uri.EMPTY) firebaseStorageRepository.deletePicture(frontPath)
            return false
        }

        return firestoreRepository.addCoaster(userId, Coaster(
            uid = coaster.uid,
            folderUid = coaster.folderUid,
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
    }

    private suspend fun deleteCoaster(userId: String, coaster: Coaster): Boolean {
        firestoreRepository.deleteCoaster(userId, coaster.uid)
        coasterRepository.deleteCoaster(coaster)
        return true
    }

    private suspend fun uploadFolder(userId: String, folder: Folder): Boolean {
        return firestoreRepository.addFolder(userId, folder.copy(uploaded = true))
    }

    private suspend fun deleteFolder(userId: String, folder: Folder): Boolean {
        firestoreRepository.deleteFolder(userId, folder.folderUid)
        folderRepository.deleteFolder(folder)
        return true
    }
}