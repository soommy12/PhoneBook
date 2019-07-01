package pl.bgn.roompoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import pl.bgn.roompoc.SingleContactActivity.Companion.EXTRA_ID
import pl.bgn.roompoc.data.Address

class AddAddressActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleAddressViewModel
    private lateinit var editCity : EditText
    private lateinit var editStreet : EditText
    private lateinit var editNumber : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        editCity = findViewById(R.id.edit_city)
        editStreet = findViewById(R.id.edit_street)
        editNumber = findViewById(R.id.edit_number)
        val btnSave = findViewById<Button>(R.id.button_save)
        supportActionBar?.title = "Add address"

        val bundle : Bundle ?= intent.extras
        val id : Int = bundle?.getInt(EXTRA_ID)!!

        viewModel = ViewModelProviders.of(this, SingleAddressViewModelFactory(application, id)).get(SingleAddressViewModel::class.java)

        btnSave.setOnClickListener {
            viewModel.insert(Address(0, id, editCity.text.toString(), editStreet.text.toString(), editNumber.text.toString().toInt()))
            finish()
        }
    }
}