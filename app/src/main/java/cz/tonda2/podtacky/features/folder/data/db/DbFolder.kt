package cz.tonda2.podtacky.features.folder.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "folders",
    foreignKeys = [
        ForeignKey(
            entity = DbFolder::class,
            parentColumns = arrayOf("folderId"),
            childColumns = arrayOf("parentId")
        )
    ]
)
data class DbFolder(
    @PrimaryKey(autoGenerate = true) val folderId: Long = 0,
    val name: String = "",
    val parentId: Long? = null,
    val uploaded: Boolean = false,
    val deleted: Boolean = false
)