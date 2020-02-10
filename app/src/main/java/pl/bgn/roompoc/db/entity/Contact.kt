package pl.bgn.roompoc.db.entity

import androidx.room.*

@Entity(tableName = "contact_table")
data class Contact(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "surname")
    val surname : String,
    val name : String,
    val number : Int
)