package cz.tonda2.podtacky.features.coaster.data.db

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoasterLocalDataSource(private val coasterDao: CoasterDao) {

    fun getCoastersStream(): LiveData<List<Coaster>> {
        return coasterDao.getCoasters().map { dbCoasters ->
            dbCoasters.map { dbCoaster ->
                dbCoaster.toDomain()
            }
        }
    }

    fun getCoastersFlow(): Flow<List<Coaster>> {
        return coasterDao.getCoastersFlow().map { dbCoasters ->
            dbCoasters.map { dbCoaster ->
                dbCoaster.toDomain()
            }
        }
    }

    suspend fun getCoaster(id: String): Coaster? = coasterDao.getCoasterById(id)?.toDomain()

    suspend fun getCoasterByUid(uid: String): Coaster? = coasterDao.getCoasterByUid(uid)?.toDomain()

    fun seachCoasters(query: String): Flow<List<Coaster>> = coasterDao.searchCoasters(query).map { dbCoasters ->
        dbCoasters.map { dbCoaster ->
            dbCoaster.toDomain()
        }
    }

    suspend fun isCoasterDuplicate(coaster: Coaster): Boolean {
        return coasterDao.getCoasterByUid(coaster.uid) != null
    }

    suspend fun markUploaded(id: String) = coasterDao.markUploaded(id)

    suspend fun markDeleted(id: String) = coasterDao.markDeleted(id)

    suspend fun insert(coaster: Coaster) {
        val dbCoaster = DbCoaster(
            uid = coaster.uid,
            brewery = coaster.brewery,
            description = coaster.description,
            dateAdded = coaster.dateAdded,
            city = coaster.city,
            count = coaster.count,
            frontUri = coaster.frontUri.toString(),
            backUri = coaster.backUri.toString(),
            uploaded = coaster.uploaded,
            deleted = coaster.deleted
        )
        coasterDao.insert(dbCoaster)
    }

    suspend fun delete(coaster: Coaster) {
        val dbCoaster = DbCoaster(
            coasterId = coaster.coasterId,
            uid = coaster.uid,
            brewery = coaster.brewery,
            description = coaster.description,
            dateAdded = coaster.dateAdded,
            city = coaster.city,
            count = coaster.count,
            frontUri = coaster.frontUri.toString(),
            backUri = coaster.backUri.toString(),
            uploaded = coaster.uploaded,
            deleted = coaster.deleted
        )
        coasterDao.delete(dbCoaster)
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