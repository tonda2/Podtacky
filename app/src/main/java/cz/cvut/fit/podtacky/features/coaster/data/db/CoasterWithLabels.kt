package cz.cvut.fit.podtacky.features.coaster.data.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation

//@Entity(
//    primaryKeys = ["coasterId", "labelId"],
//    indices = [Index(value = ["coasterId"]), Index(value = ["labelId"])]
//)
//data class CoastersLabelsCrossRef(
//    val coasterId: Long,
//    val labelId: Long
//)
//
//data class CoasterWithLabels(
//    @Embedded val coaster: DbCoaster,
//
//    @Relation(
//        parentColumn = "coasterId",
//        entityColumn = "labelId",
//        associateBy = Junction(CoastersLabelsCrossRef::class)
//    )
//    val labels: List<DbLabel>
//)
