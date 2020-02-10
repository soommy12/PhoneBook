package pl.bgn.roompoc.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "building_table",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["companyId"],
            onDelete = CASCADE
        )
    ])
data class Building(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val companyId: Int,
    val city: String,
    val street: String
)