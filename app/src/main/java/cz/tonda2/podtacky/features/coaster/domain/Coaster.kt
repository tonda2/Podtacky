package cz.tonda2.podtacky.features.coaster.domain

import android.net.Uri

data class Coaster(
    val coasterId: Long = 0L,
    val uid: String,
    val folderId: Long?,
    val brewery: String,
    val description: String,
    val dateAdded: String,
    val city: String,
    val count: Int,
    val frontUri: Uri,
    val backUri: Uri,
    val uploaded: Boolean,
    val deleted: Boolean
)