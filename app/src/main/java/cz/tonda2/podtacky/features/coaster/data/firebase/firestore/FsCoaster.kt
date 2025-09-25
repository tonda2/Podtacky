package cz.tonda2.podtacky.features.coaster.data.firebase.firestore

data class FsCoaster(
    val uid: String = "",
    val folderUid: String? = null,
    val brewery: String = "",
    val description: String = "",
    val dateAdded: String = "",
    val city: String = "",
    val count: Int = 0,
    val frontUri: String = "",
    val backUri: String = "",
    val uploaded: Boolean = true,
    val deleted: Boolean = false
)
