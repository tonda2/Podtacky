package cz.tonda2.podtacky.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.tonda2.podtacky.features.coaster.data.db.CoasterDao
import cz.tonda2.podtacky.features.coaster.data.db.DbCoaster

@Database(version = 1, entities = [DbCoaster::class])
abstract class CoasterDatabase : RoomDatabase() {

    abstract fun coasterDao(): CoasterDao

    companion object {
        fun newInstance(context: Context): CoasterDatabase {
            return Room.databaseBuilder(context, CoasterDatabase::class.java, "coaster.db").build()
        }
    }
}