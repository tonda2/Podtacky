package cz.tonda2.podtacky.features.coaster.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.Normalizer

@Entity(
    tableName = "coasters",
    indices = [Index(value = ["normalizedSearchText"], name = "coaster_normalized_text_index")]
)
data class DbCoaster(
    @PrimaryKey(autoGenerate = true) val coasterId: Long = 0,
    val folderUid: String? = null,
    val uid: String = "",
    val brewery: String = "",
    val description: String = "",
    val dateAdded: String = "",
    val city: String = "",
    val count: Int = 0,
    val frontUri: String = "",
    val backUri: String = "",
    val uploaded: Boolean = false,
    val deleted: Boolean = false,
    @ColumnInfo(defaultValue = "")
    val normalizedSearchText: String = ""
)

fun String.normalizeForSearch(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("[\\p{M}]".toRegex(), "")
        .lowercase()
}

fun buildNormalizedSearchString(vararg strings: String) =
    strings.joinToString(separator = " ", transform = { it.normalizeForSearch() })