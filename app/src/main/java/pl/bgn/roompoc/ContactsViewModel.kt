package pl.bgn.roompoc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.data.Contact
import pl.bgn.roompoc.data.ContactsRepository
import pl.bgn.roompoc.data.MyRoomDatabase

class ContactsViewModel(application: Application) : AndroidViewModel(application){

    private val contactRepository: ContactsRepository
    var contacts: LiveData<List<Contact>>

    init {
        val contactDao = MyRoomDatabase.getDatabase(application, viewModelScope).contactDao()
        contactRepository = ContactsRepository(contactDao)
        contacts = contactRepository.contacts
    }

    fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) { contactRepository.delete(contact)}

    fun search(searchText: String?) = viewModelScope.launch(Dispatchers.IO) {
        contacts = contactRepository.getSearchContacts(searchText)
    }

    fun getAllContacts() = viewModelScope.launch(Dispatchers.IO) { contactRepository.contacts }
}