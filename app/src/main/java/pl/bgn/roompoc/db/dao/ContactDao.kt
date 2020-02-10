package pl.bgn.roompoc.db.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import pl.bgn.roompoc.db.entity.Address
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

    @Query("SELECT * FROM contact_table WHERE id =:addressId")
    fun getContactForAddress(addressId: Int) : LiveData<Contact>

    @Query("SELECT name, id FROM contact_table")
    fun getContactWithId(): Cursor
}