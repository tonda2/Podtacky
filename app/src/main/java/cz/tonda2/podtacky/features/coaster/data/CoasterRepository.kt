package cz.tonda2.podtacky.features.coaster.data

import android.net.Uri
import cz.tonda2.podtacky.features.coaster.data.db.CoasterDao
import cz.tonda2.podtacky.features.coaster.data.db.DbCoaster
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoasterRepository(
    private val coasterDao: CoasterDao
) {

    fun getAllCoasters(): Flow<List<Coaster>> {
        return coasterDao.getCoasters().map { list ->
            list.map { dbCoaster -> dbCoaster.toDomain() }
        }
    }

    fun getUndeletedCoastersList(): Flow<List<Coaster>> {
        return coasterDao.getCoasters().map { list ->
            list.map { dbCoaster ->
                dbCoaster.toDomain()
            }.filter { dbCoaster ->
                !dbCoaster.deleted
            }
        }
    }

    fun getCoastersInFolder(folderId: String): Flow<List<Coaster>> {
        return coasterDao.getCoastersByFolder(folderId).map { list ->
            list.map { dbCoaster -> dbCoaster.toDomain() }
        }
    }

    fun getCoastersWithoutFolder(): Flow<List<Coaster>> {
        return coasterDao.getCoastersWithoutFolder().map { list ->
            list.map { dbCoaster -> dbCoaster.toDomain() }
        }
    }

    suspend fun getCoasterById(id: String): Coaster? {
        val coaster = coasterDao.getCoasterById(id)
        return coaster?.toDomain()
    }

    fun searchCoasters(query: String): Flow<List<Coaster>> {
        return coasterDao.searchCoasters(query).map { list ->
            list.map { dbCoaster -> dbCoaster.toDomain() }
        }
    }

    suspend fun isCoasterDuplicate(coaster: Coaster): Boolean {
        return coasterDao.getCoasterByUid(coaster.uid) != null
    }

    suspend fun markUploaded(id: String) {
        coasterDao.markUploaded(id)
    }

    suspend fun markDeleted(id: String) {
        coasterDao.markDeleted(id)
    }

    suspend fun addCoaster(coaster: Coaster) {
        coasterDao.insert(coaster.toDb())
    }

    suspend fun deleteCoaster(coaster: Coaster) {
        return coasterDao.delete(coaster.toDb())
    }
}

fun DbCoaster.toDomain(): Coaster {
    return Coaster(
        uid = uid,
        coasterId = coasterId,
        brewery = brewery,
        description = description,
        dateAdded = dateAdded,
        city = city,
        count = count,
        frontUri = Uri.parse(frontUri) ?: Uri.EMPTY,
        backUri = Uri.parse(backUri) ?: Uri.EMPTY,
        uploaded = uploaded,
        deleted = deleted
    )
}

fun Coaster.toDb(): DbCoaster {
    return DbCoaster(
        coasterId = coasterId,
        uid = uid,
        brewery = brewery,
        description = description,
        dateAdded = dateAdded,
        city = city,
        count = count,
        frontUri = frontUri.toString(),
        backUri = backUri.toString(),
        uploaded = uploaded,
        deleted = deleted
    )
}