package cz.cvut.fit.podtacky.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.cvut.fit.podtacky.features.coaster.data.db.CoasterDao
import cz.cvut.fit.podtacky.features.coaster.data.db.Converters
import cz.cvut.fit.podtacky.features.coaster.data.db.DbCoaster

@Database(version = 1, entities = [DbCoaster::class])
@TypeConverters(Converters::class)
abstract class CoasterDatabase : RoomDatabase() {

    abstract fun coasterDao(): CoasterDao

    companion object {
        fun newInstance(context: Context): CoasterDatabase {
            return Room.databaseBuilder(context, CoasterDatabase::class.java, "coaster.db").build()
        }
    }
}