package pl.bgn.roompoc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.bgn.roompoc.data.Contact
import java.lang.RuntimeException

class ContactListAdapter internal constructor(context: Context) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contacts = emptyList<Contact>() //Cached copy of contacts
    private val listener = if (context is MainActivity) context else throw RuntimeException("Activity must implement OnContactListener interface!")

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val namesurnameView: TextView = itemView.findViewById(R.id.name_surname)
        val phoneView: TextView = itemView.findViewById(R.id.phone)
        var view: View = itemView

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onContactClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = contacts[position]
        val name = current.surname + " " + current.name
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