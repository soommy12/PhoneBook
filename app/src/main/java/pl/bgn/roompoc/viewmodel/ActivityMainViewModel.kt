package pl.bgn.roompoc.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.db.entity.Contact
import pl.bgn.roompoc.db.ContactsRepository
import pl.bgn.roompoc.db.MyRoomDatabase

class ActivityMainViewModel(application: Application) : AndroidViewModel(application){

    private val contactRepository: ContactsRepository
    private val searchInput = MutableLiveData<String>()
    var contacts: LiveData<List<Contact>>

    init {
        val contactDao = MyRoomDatabase.getDatabase(application, viewModelScope).contactDao()
        contactRepository = ContactsRepository(contactDao)
        setSearchValue("")
        // jezeli searchInput sie zmieni to wtedy wykonujemy szukanie, jezeli searchText jest pusty to bierzemy wszystko
        contacts = Transformations.switchMap(searchInput) {
                searchText ->
                    if(searchText == "")
                        contactRepository.contacts
                    else contactRepository.getSearchContacts(searchText)
        }
    }

    fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) { contactRepository.delete(contact)}

    fun setSearchValue(searchText: String){
        searchInput.value = searchText
    }
}