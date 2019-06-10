package pl.bgn.roompoc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.data.*

class ContactViewModel(application: Application) : AndroidViewModel(application){

    private val repository: WordRepository
    val allWords: LiveData<List<Word>>
    private val contactRepository: ContactsRepository
    val allContacts: LiveData<List<Contact>>

    init {
        val wordsDao = MyRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords

        val contactDao = MyRoomDatabase.getDatabase(application, viewModelScope).contactDao()
        contactRepository = ContactsRepository(contactDao)
        allContacts = contactRepository.allContacts
    }

    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }

    fun insert(contact: Contact) = viewModelScope.launch(Dispatchers.IO) { contactRepository.insert(contact)}

    fun update(contact: Contact) = viewModelScope.launch(Dispatchers.IO) { contactRepository.update(contact)}

    fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) { contactRepository.delete(contact)}
}