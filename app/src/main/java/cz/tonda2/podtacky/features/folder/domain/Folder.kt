package cz.tonda2.podtacky.features.folder.domain

data class Folder(
    val folderId: Long = 0L,
    val uid: String,
    val name: String,
    val parentId: Long?,
    val uploaded: Boolean,
    val deleted: Boolean
)