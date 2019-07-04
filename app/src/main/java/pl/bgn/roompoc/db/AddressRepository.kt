package pl.bgn.roompoc.db

import androidx.annotation.WorkerThread
import pl.bgn.roompoc.db.dao.AddressDao
import pl.bgn.roompoc.db.entity.Address

class AddressRepository(private val addressDao: AddressDao) {

    @WorkerThread
    suspend fun insert(address: Address) = addressDao.insert(address)

    @WorkerThread
    suspend fun update(address: Address) = addressDao.update(address)

    @WorkerThread
    suspend fun delete(address: Address) = addressDao.delete(address)

    @WorkerThread
    fun getContactAdresses(userId: Int) = addressDao.getAddressesForContact(userId)

    @WorkerThread
    fun getAddress(addressId: Int) = addressDao.getAddress(addressId)
}