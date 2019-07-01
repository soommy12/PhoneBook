package pl.bgn.roompoc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import pl.bgn.roompoc.data.Address
import pl.bgn.roompoc.data.Contact
import java.lang.RuntimeException

class ContactListAdapter internal constructor(private val context: Context) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contacts = emptyList<Contact>() // scashowane kontakty
    private val listener = if (context is MainActivity) context else throw RuntimeException("Activity must implement OnContactListener interface!")

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val namesurnameView: TextView = itemView.findViewById(R.id.name_surname)
        val phoneView: TextView = itemView.findViewById(R.id.phone)
        val addressesLayout: LinearLayout = itemView.findViewById(R.id.layout_addresses)
        val editBtn: ImageView = itemView.findViewById(R.id.edit_contact)
        var addresses = emptyList<Address>()

        init {
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
//            listener.onContactClick(adapterPosition)
            if(addressesLayout.visibility == View.VISIBLE) addressesLayout.visibility = View.GONE
            else addressesLayout.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = contacts[position]
        val name = current.surname + " " + current.name
        holder.editBtn.setOnClickListener { listener.onContactClick(position) }
        holder.namesurnameView.text = name
        holder.phoneView.text = current.number.toString()
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