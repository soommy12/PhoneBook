package pl.bgn.roompoc.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AddressDao {

    @Insert
    suspend fun insert(address: Address)

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)

    @Query("SELECT * FROM address_table WHERE id =:contactId")
    fun getAddressesForContact(contactId: Int) : LiveData<List<Address>>
}