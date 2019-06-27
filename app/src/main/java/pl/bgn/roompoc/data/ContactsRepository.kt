package pl.bgn.roompoc.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ContactsRepository(private val contactDao: ContactDao) {
    var contacts: LiveData<List<Contact>> = contactDao.getAllContacts()

    @WorkerThread
    suspend fun insert(contact: Contact) = contactDao.insert(contact)

    @WorkerThread
    suspend fun update(contact: Contact) = contactDao.update(contact)

    @WorkerThread
    suspend fun delete(contact: Contact) = contactDao.delete(contact)

    @WorkerThread
    fun getContact(id :Int) = contactDao.getContact(id)

    @WorkerThread
    fun getSearchContacts(text: String?) = contactDao.getSearchContacts(text)
}