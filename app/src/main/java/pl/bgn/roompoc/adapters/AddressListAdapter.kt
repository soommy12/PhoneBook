package pl.bgn.roompoc.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pl.bgn.roompoc.R
import pl.bgn.roompoc.db.entity.Address
import pl.bgn.roompoc.databinding.RecyclerviewAddressItemBinding
import pl.bgn.roompoc.ui.MainActivity
import java.lang.RuntimeException

class AddressListAdapter internal constructor(context: Context) : RecyclerView.Adapter<AddressListAdapter.AddressViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var addresses = emptyList<Address>() // scashowane adresy
    private val listener = if (context is MainActivity) context else throw RuntimeException("Activity must implement OnAddressListener interface!")

    inner class AddressViewHolder(val binding: RecyclerviewAddressItemBinding, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address) {
            with(binding){
                nameSurname.text = address.street + " " + address.number.toString()
                phone.text = address.city
//                editContact.setOnClickListener { listener.onContactClick(adapterPosition) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding : RecyclerviewAddressItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_address_item, parent, false)
        return AddressViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addresses[position])
    }

    internal fun setAddresses(addresses: List<Address>){
        Log.e("Addreses", "Mamy $addresses")
        this.addresses = addresses
        notifyDataSetChanged()
    }

    override fun getItemCount() = addresses.size

    interface OnAddressListener {
        fun onAddressEditClick(position: Int)
    }
}