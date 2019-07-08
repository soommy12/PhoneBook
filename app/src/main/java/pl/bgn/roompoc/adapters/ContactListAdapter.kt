package pl.bgn.roompoc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.bgn.roompoc.R
import pl.bgn.roompoc.databinding.RecyclerviewContactItemBinding
import pl.bgn.roompoc.db.entity.Address
import pl.bgn.roompoc.db.entity.Contact
import pl.bgn.roompoc.ui.MainActivity

class ContactListAdapter internal constructor(ctx: Context) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private var contacts = emptyList<Contact>() // cashed contacts
    private var addresses = emptyList<Address>() // cashed addresses
    private lateinit var contactAddress : List<Address>
    private val listener = if (ctx is MainActivity) ctx else throw RuntimeException("Activity must implement OnContactListener interface!")


    inner class ContactViewHolder(private val binding: RecyclerviewContactItemBinding, private val context : Context ) : RecyclerView.ViewHolder(binding.root) {



        fun bind(contact: Contact) {
            with(binding){
                nameSurname.text = contact.name + " " + contact.surname
                phone.text = contact.number.toString()
                editContact.setOnClickListener {
                    listener.onContactClick(adapterPosition) }
                val adapter = AddressListAdapter(context)
                contactAddress = getContactAddresses(contact.id)
                adapter.setAddresses(contactAddress)
                recyclerviewAddresses.adapter = adapter
                recyclerviewAddresses.layoutManager = LinearLayoutManager(context)
                root.setOnClickListener {
                    layoutAddresses.visibility = if(layoutAddresses.visibility == View.GONE) View.VISIBLE else View.GONE
                }
                layoutAddresses.setOnClickListener {
                    listener.onAddAddress(contact.id)
                }
                val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val address = contactAddress[position]
                        listener.onSwipeAddress(address)
                    }

                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }
                }
                val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
                itemTouchHelper.attachToRecyclerView(recyclerviewAddresses)
            }
        }
    }

    private fun getContactAddresses(id: Int): List<Address> {
        val contactAddresses = ArrayList<Address>()
        for(address in addresses)
            if(address.contactId == id) contactAddresses.add(address)
        return contactAddresses
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding : RecyclerviewContactItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_contact_item, parent, false)
        return ContactViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    internal fun setContacts(contacts: List<Contact>){
        this.contacts = contacts
        notifyDataSetChanged()
    }

    internal fun setAddresses(addresses: List<Address>) {
        this.addresses = addresses
        notifyDataSetChanged()
    }

    override fun getItemCount() = contacts.size


    interface OnContactListener {
        fun onContactClick(position: Int)
        fun onAddAddress(contactID: Int)
        fun onSwipeAddress(address: Address)
    }
}