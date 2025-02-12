package cz.tonda2.podtacky.features.folder.domain

data class Folder(
    val folderUid: String,
    val name: String,
    val parentUid: String?,
    val uploaded: Boolean,
    val deleted: Boolean
)