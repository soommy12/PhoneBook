package pl.bgn.roompoc.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_table")
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val city: String,
    val street: String,
    val number: Int
)