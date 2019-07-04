package pl.bgn.roompoc.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "address_table",
    foreignKeys = [
        ForeignKey(
        entity = Contact::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = CASCADE)
    ])
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val contactId: Int,
    val city: String,
    val street: String,
    val number: Int
)