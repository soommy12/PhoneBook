package pl.bgn.roompoc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.bgn.roompoc.R
import pl.bgn.roompoc.adapters.AddressListAdapter
import pl.bgn.roompoc.viewmodel.AddressesViewModel
import pl.bgn.roompoc.viewmodel.AddressesViewModelFactory

class AddressFragment : Fragment() {

    private var contactId: Int = 0
    private var columnCount = 1
    lateinit var addressesViewModel: AddressesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            contactId = it.getInt(ARG_CONTACT_ID)
            addressesViewModel = ViewModelProviders.of(this,
                AddressesViewModelFactory(activity!!.application, contactId)
            ).get(AddressesViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_address_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val addressAdapter = AddressListAdapter(context)
                adapter = addressAdapter
                addressesViewModel.contactAddresses.observe(this@AddressFragment, Observer {
                    addresses -> addresses?.let { addressAdapter.setAddresses(it) }
                })
            }
        }
        return view
    }

    companion object {

         const val ARG_CONTACT_ID = "column-id"

        @JvmStatic
        fun newInstance(contactId: Int) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CONTACT_ID, columnCount)
                }
            }
    }
}
