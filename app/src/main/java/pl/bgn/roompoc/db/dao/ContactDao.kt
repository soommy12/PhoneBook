package pl.bgn.roompoc.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.bgn.roompoc.db.entity.Contact

@Dao
interface ContactDao {

    @Insert
    suspend fun insert(contact : Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * FROM contact_table ORDER BY surname ASC")
    fun getAllContacts() : LiveData<List<Contact>>

    @Query("SELECT * FROM contact_table WHERE id = :id")
    fun getContact(id :Int) : Contact

    @Query("SELECT * FROM contact_table WHERE name LIKE :queryText OR surname LIKE :queryText")
    fun getSearchContacts(queryText: String?) : LiveData<List<Contact>>
}