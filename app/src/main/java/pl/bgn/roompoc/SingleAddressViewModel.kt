package pl.bgn.roompoc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.data.Address
import pl.bgn.roompoc.data.AddressRepository
import pl.bgn.roompoc.data.MyRoomDatabase

class SingleAddressViewModel(application: Application, contactIndex: Int) : AndroidViewModel(application){

    private val addressRepository : AddressRepository
    val contactAddresses : LiveData<List<Address>>

    init {
        val addressDao = MyRoomDatabase.getDatabase(application, viewModelScope).addressDao()
        addressRepository = AddressRepository(addressDao)
        contactAddresses = addressRepository.getContactAdresses(contactIndex)
    }

    fun insert(address: Address) = viewModelScope.launch(Dispatchers.IO) { addressRepository.insert(address) }

    fun update(address: Address) = viewModelScope.launch(Dispatchers.IO) { addressRepository.update(address)}
}