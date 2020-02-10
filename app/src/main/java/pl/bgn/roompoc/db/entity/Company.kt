package pl.bgn.roompoc.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "company_table")
data class Company(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)