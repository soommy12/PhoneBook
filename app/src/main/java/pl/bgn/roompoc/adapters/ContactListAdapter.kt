package pl.bgn.roompoc.adapters

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.bgn.roompoc.R
import pl.bgn.roompoc.databinding.RecyclerviewContactItemNewBinding
import pl.bgn.roompoc.db.entity.Contact
import pl.bgn.roompoc.ui.MainActivity
import pl.bgn.roompoc.viewmodel.AddressesViewModel
import pl.bgn.roompoc.viewmodel.AddressesViewModelFactory

class ContactListAdapter internal constructor(val ctx: Context) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private var contacts = emptyList<Contact>() // cashed contacts
    private val listener = if (ctx is MainActivity) ctx else throw RuntimeException("Activity must implement OnContactListener interface!")
    private val TAG = "ContactListAdapter"

    inner class ContactViewHolder(val binding: RecyclerviewContactItemNewBinding, val context : Context ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var addressesViewModel: AddressesViewModel

        fun bind(contact: Contact) {
            with(binding){
                nameSurname.text = contact.name + " " + contact.surname
                phone.text = contact.number.toString()
                editContact.setOnClickListener {
                    listener.onContactClick(adapterPosition) }
                val adapter = AddressListAdapter(context)
                recyclerviewAddresses.adapter = adapter
                recyclerviewAddresses.layoutManager = LinearLayoutManager(context)

                addressesViewModel.contactAddresses.observe(context as FragmentActivity, Observer { addresses ->
                    addresses?.let {
                        adapter.setAddresses(it)
                    }
                })



                root.setOnClickListener {
                    recyclerviewAddresses.visibility = if(recyclerviewAddresses.visibility == View.GONE) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding : RecyclerviewContactItemNewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_contact_item_new, parent, false)
        return ContactViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val vm : AddressesViewModel = ViewModelProviders.of(holder.itemView.context as FragmentActivity,
            AddressesViewModelFactory(ctx.applicationContext as Application, contacts[position].id)
        ).get(AddressesViewModel::class.java)
        holder.itemView.context
        Log.e(TAG, "ID:" + contacts[position].id)
        Log.e(TAG, "vm: $vm")
        holder.addressesViewModel = vm
        holder.bind(contacts[position])
    }

    internal fun setContacts(contacts: List<Contact>){
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun getItemCount() = contacts.size

    interface OnContactListener {
        fun onContactClick(position: Int)
    }
}