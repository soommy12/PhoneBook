package pl.bgn.roompoc.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.bgn.roompoc.db.entity.Address

@Dao
interface AddressDao {

    @Insert
    suspend fun insert(address: Address)

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)

    @Query("SELECT * FROM address_table WHERE contactId =:contactId")
    fun getAddressesForContact(contactId: Int) : LiveData<List<Address>>

    @Query("SELECT * FROM address_table WHERE id = :addressId")
    fun getAddress(addressId: Int) : Address

    @Query("SELECT * FROM address_table")
    fun getAllAddresses(): LiveData<List<Address>>
}