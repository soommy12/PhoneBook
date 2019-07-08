package pl.bgn.roompoc.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.db.AddressRepository
import pl.bgn.roompoc.db.entity.Contact
import pl.bgn.roompoc.db.ContactsRepository
import pl.bgn.roompoc.db.MyRoomDatabase
import pl.bgn.roompoc.db.entity.Address

class ContactsViewModel(application: Application) : AndroidViewModel(application){

    private val contactRepository: ContactsRepository
    private val addressRepository: AddressRepository

    private val searchInput = MutableLiveData<String>()
    var contacts: LiveData<List<Contact>>
    var addresses: LiveData<List<Address>>

    init {
        val contactDao = MyRoomDatabase.getDatabase(application, viewModelScope).contactDao()
        val addressDao = MyRoomDatabase.getDatabase(application, viewModelScope).addressDao()
        contactRepository = ContactsRepository(contactDao)
        addressRepository = AddressRepository(addressDao)
        setSearchValue("")
        // jezeli searchInput sie zmieni to wtedy wykonujemy szukanie, jezeli searchText jest pusty to bierzemy wszystko
        contacts = Transformations.switchMap(searchInput) { searchText ->
            if (searchText == "")
                contactRepository.contacts
            else contactRepository.getSearchContacts(searchText)
        }
        addresses = addressRepository.getAllAddresses()
    }

    fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) { contactRepository.delete(contact)}

    fun deleteAddress(address: Address) = viewModelScope.launch(Dispatchers.IO) { addressRepository.delete(address)}

    fun setSearchValue(searchText: String){
        searchInput.value = searchText
    }
}