package cz.cvut.fit.podtacky.features.coaster.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coasters")
data class DbCoaster(
    @PrimaryKey(autoGenerate = true) val coasterId: Long = 0,
    val brewery: String = "",
    val description: String = "",
    val dateAdded: String = "",
    val city: String = "",
    val count: Int = 0,
    val frontUri: String = "",
    val backUri: String = "",
    val uploaded: Boolean = false,
    val deleted: Boolean = false
)

