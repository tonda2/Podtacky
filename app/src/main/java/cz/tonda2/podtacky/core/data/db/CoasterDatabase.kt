package cz.tonda2.podtacky.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cz.tonda2.podtacky.features.coaster.data.db.CoasterDao
import cz.tonda2.podtacky.features.coaster.data.db.DbCoaster
import cz.tonda2.podtacky.features.coaster.data.db.buildNormalizedSearchString
import cz.tonda2.podtacky.features.folder.data.db.DbFolder
import cz.tonda2.podtacky.features.folder.data.db.FolderDao

@Database(
    version = 3,
    entities = [DbCoaster::class, DbFolder::class]
)
abstract class CoasterDatabase : RoomDatabase() {

    abstract fun coasterDao(): CoasterDao

    abstract fun folderDao(): FolderDao

    companion object {
        fun newInstance(context: Context): CoasterDatabase {
            return Room.databaseBuilder(context, CoasterDatabase::class.java, "coaster.db")
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS folders (
                folderUid TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                parentUid TEXT,
                uploaded INTEGER NOT NULL DEFAULT 0,
                deleted INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(parentUid) REFERENCES folders(folderUid) ON DELETE NO ACTION
            )
        """.trimIndent())

        db.execSQL("ALTER TABLE coasters ADD COLUMN folderUid TEXT")
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE coasters ADD COLUMN normalizedSearchText TEXT NOT NULL DEFAULT ''")

        db.query("SELECT coasterId, brewery, description FROM coasters").use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getString(0)
                val brewery = cursor.getString(1)
                val description = cursor.getString(2)
                val normalizedSearchText = buildNormalizedSearchString(brewery, description)

                db.execSQL(
                    "UPDATE coasters SET normalizedSearchText = ? WHERE coasterId = ?",
                    arrayOf(normalizedSearchText, id)
                )
            }
        }

        db.execSQL("CREATE INDEX IF NOT EXISTS coaster_normalized_text_index ON coasters(normalizedSearchText)")
    }
}