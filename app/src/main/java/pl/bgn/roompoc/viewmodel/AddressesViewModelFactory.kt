package pl.bgn.roompoc.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddressesViewModelFactory(val application : Application, val contactIndex : Int) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddressesViewModel(application, contactIndex) as T
    }
}