package pl.bgn.roompoc.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val surname : String,
    val name : String,
    val number : Int
)