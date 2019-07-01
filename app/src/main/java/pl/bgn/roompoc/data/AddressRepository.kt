package pl.bgn.roompoc.data

import androidx.annotation.WorkerThread

class AddressRepository(private val addressDao: AddressDao) {

    @WorkerThread
    suspend fun insert(address: Address) = addressDao.insert(address)

    @WorkerThread
    suspend fun update(address: Address) = addressDao.update(address)

    @WorkerThread
    suspend fun delete(address: Address) = addressDao.delete(address)

    @WorkerThread
    fun getContactAdresses(userId: Int) = addressDao.getAddressesForContact(userId)
}