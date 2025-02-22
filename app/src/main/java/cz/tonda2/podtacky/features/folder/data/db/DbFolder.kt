package cz.tonda2.podtacky.features.folder.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "folders",
    foreignKeys = [
        ForeignKey(
            entity = DbFolder::class,
            parentColumns = arrayOf("folderUid"),
            childColumns = arrayOf("parentUid")
        )
    ]
)
data class DbFolder(
    @PrimaryKey(autoGenerate = false) val folderUid: String = "",
    val name: String = "",
    val parentUid: String? = null,
    val uploaded: Boolean = false,
    val deleted: Boolean = false
)