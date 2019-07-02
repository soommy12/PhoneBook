package pl.bgn.roompoc

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pl.bgn.roompoc.data.Contact
import pl.bgn.roompoc.databinding.RecyclerviewAddresItemNewBinding
import java.lang.RuntimeException

class ContactListAdapter internal constructor(context: Context) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contacts = emptyList<Contact>() // scashowane kontakty
    private val listener = if (context is MainActivity) context else throw RuntimeException("Activity must implement OnContactListener interface!")

    inner class ContactViewHolder(val binding: RecyclerviewAddresItemNewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            with(binding){
                nameSurname.text = contact.name + " " + contact.surname
                phone.text = contact.number.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding : RecyclerviewAddresItemNewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_addres_item_new, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
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