package cz.cvut.fit.podtacky.features.coaster.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "labels")
data class DbLabel(
    @PrimaryKey val labelId: Long,
    val title: String
)
