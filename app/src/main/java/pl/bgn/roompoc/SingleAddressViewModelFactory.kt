package pl.bgn.roompoc

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SingleAddressViewModelFactory(val application : Application, val contactIndex : Int) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SingleAddressViewModel(application, contactIndex) as T
    }
}