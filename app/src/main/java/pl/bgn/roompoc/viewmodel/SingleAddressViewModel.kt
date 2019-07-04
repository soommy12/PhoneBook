package pl.bgn.roompoc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.db.AddressRepository
import pl.bgn.roompoc.db.MyRoomDatabase
import pl.bgn.roompoc.db.entity.Address

class SingleAddressViewModel(application: Application) : AndroidViewModel(application) {

    private val addressRepository: AddressRepository

    init {
        val addressDao = MyRoomDatabase.getDatabase(application, viewModelScope).addressDao()
        addressRepository = AddressRepository(addressDao)
    }

    fun getAddress(id :Int): Address = addressRepository.getAddress(id)

    fun insert(address: Address) = viewModelScope.launch(Dispatchers.IO) { addressRepository.insert(address)}

    fun update(address: Address) = viewModelScope.launch(Dispatchers.IO) { addressRepository.update(address)}
}