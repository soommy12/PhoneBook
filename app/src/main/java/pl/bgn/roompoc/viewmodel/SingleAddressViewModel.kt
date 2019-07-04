package pl.bgn.roompoc.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.db.entity.Address
import pl.bgn.roompoc.db.AddressRepository
import pl.bgn.roompoc.db.MyRoomDatabase

class SingleAddressViewModel(application: Application, contactIndex: Int) : AndroidViewModel(application){

    private val addressRepository : AddressRepository
    val contactAddresses : LiveData<List<Address>>

    init {
        val addressDao = MyRoomDatabase.getDatabase(application, viewModelScope).addressDao()
        addressRepository = AddressRepository(addressDao)
        Log.e("INIT", "idx: $contactIndex")
        contactAddresses = addressRepository.getContactAdresses(contactIndex)
    }

    fun insert(address: Address) = viewModelScope.launch(Dispatchers.IO) { Log.e("insert", "adding for: ${address.contactId}")
        addressRepository.insert(address) }

    fun update(address: Address) = viewModelScope.launch(Dispatchers.IO) { addressRepository.update(address)}
}