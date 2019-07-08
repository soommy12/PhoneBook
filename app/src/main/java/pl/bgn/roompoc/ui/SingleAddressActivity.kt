package pl.bgn.roompoc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import pl.bgn.roompoc.R
import pl.bgn.roompoc.ui.SingleContactActivity.Companion.EXTRA_CONTACT_ID
import pl.bgn.roompoc.db.entity.Address
import pl.bgn.roompoc.viewmodel.SingleAddressViewModel

class SingleAddressActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleAddressViewModel
    private lateinit var editCity : EditText
    private lateinit var editStreet : EditText
    private lateinit var editNumber : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        editCity = findViewById(R.id.edit_city)
        editStreet = findViewById(R.id.edit_street)
        editNumber = findViewById(R.id.edit_number)
        val btnSave = findViewById<Button>(R.id.button_save)
        supportActionBar?.title = "Add address"

        val bundle : Bundle ?= intent.extras
        val contactId : Int = bundle?.getInt(EXTRA_CONTACT_ID)!!

        viewModel = ViewModelProviders.of(this).get(SingleAddressViewModel::class.java)

        btnSave.setOnClickListener {
            viewModel.insert(
                Address(
                    0,
                    contactId,
                    editCity.text.toString(),
                    editStreet.text.toString(),
                    editNumber.text.toString().toInt()
                )
            )
            finish()
        }
    }
}
