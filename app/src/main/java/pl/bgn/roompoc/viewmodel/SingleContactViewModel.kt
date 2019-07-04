package pl.bgn.roompoc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.db.entity.Contact
import pl.bgn.roompoc.db.ContactsRepository
import pl.bgn.roompoc.db.MyRoomDatabase

class SingleContactViewModel(application: Application) : AndroidViewModel(application) {

    private val contactRepository: ContactsRepository
    private val allContacts: LiveData<List<Contact>>

    init {
        val contactDao = MyRoomDatabase.getDatabase(application, viewModelScope).contactDao()
        contactRepository = ContactsRepository(contactDao)
        allContacts = contactRepository.contacts
    }

    fun getContact(id :Int): Contact = contactRepository.getContact(id)

    fun insert(contact: Contact) = viewModelScope.launch(Dispatchers.IO) { contactRepository.insert(contact)}

    fun update(contact: Contact) = viewModelScope.launch(Dispatchers.IO) { contactRepository.update(contact)}
}