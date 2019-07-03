package pl.bgn.roompoc

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
import pl.bgn.roompoc.data.Contact
import pl.bgn.roompoc.databinding.RecyclerviewContactItemNewBinding
import java.lang.RuntimeException

class ContactListAdapter internal constructor(context: Context) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contacts = emptyList<Contact>() // scashowane kontakty
    private val listener = if (context is MainActivity) context else throw RuntimeException("Activity must implement OnContactListener interface!")

    inner class ContactViewHolder(val binding: RecyclerviewContactItemNewBinding, val context : Context ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            with(binding){
                nameSurname.text = contact.name + " " + contact.surname
                phone.text = contact.number.toString()
                editContact.setOnClickListener { Log.e("TAG", "EDIT_Clicked")
                    listener.onContactClick(adapterPosition) }
                val adapter = AddressListAdapter(context)
                recyclerviewAddresses.adapter = adapter
                viewmodel!!.contactAddresses.observe(context as FragmentActivity, Observer { addresses ->
                    addresses?.let {
                        Log.e("OBSErVER", "for adresses called")
                        adapter.setAddresses(it)
                    }
                })
                recyclerviewAddresses.layoutManager = LinearLayoutManager(context)
                root.setOnClickListener {
                    Log.e("TAG", "Contact clicked")
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
        val vm : SingleAddressViewModel = ViewModelProviders.of(holder.context as FragmentActivity, SingleAddressViewModelFactory(holder.context.application, contacts[position].id)).get(SingleAddressViewModel::class.java)
        holder.binding.viewmodel = vm
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