package pl.bgn.roompoc.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.R
import pl.bgn.roompoc.db.entity.Contact
import pl.bgn.roompoc.viewmodel.SingleContactViewModel

class SingleContactActivity : AppCompatActivity() {

    private lateinit var editNameView: EditText
    private lateinit var editSurnameView: EditText
    private lateinit var editPhoneNumberView: EditText
    private lateinit var viewModel: SingleContactViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        editNameView = findViewById(R.id.edit_name)
        editSurnameView = findViewById(R.id.edit_surname)
        editPhoneNumberView = findViewById(R.id.edit_phone)

        val bundle : Bundle ?= intent.extras
        val id = bundle?.getInt(EXTRA_CONTACT_ID)
        supportActionBar?.title = "Add new contact"
        viewModel = ViewModelProviders.of(this).get(SingleContactViewModel::class.java)
        if(id != null) {
            supportActionBar?.title = "Edit contact"
            CoroutineScope(Dispatchers.IO).launch {
                val contact = viewModel.getContact(id)
                editNameView.setText(contact.name)
                editSurnameView.setText(contact.surname)
                editPhoneNumberView.setText(contact.number.toString())
            }
        }

        val buttonSave = findViewById<Button>(R.id.button_save)
        buttonSave.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editNameView.text) || TextUtils.isEmpty(editSurnameView.text)
                || TextUtils.isEmpty(editPhoneNumberView.text)) {
                setResult(RESULT_NO_DATA, replyIntent)
            } else {
                val name = editNameView.text.toString()
                val surname = editSurnameView.text.toString()
                val phone : Int = editPhoneNumberView.text.toString().toInt()
                if(id != null) {
                    viewModel.update(Contact(id, surname, name, phone))
                    setResult(RESULT_UPDATE, Intent())
                } else {
                    viewModel.insert(Contact(0, surname, name, phone))
                    setResult(RESULT_INSERT, Intent())
                }
            }
            finish()
        }

    }

    companion object {
        const val RESULT_NO_DATA = -2
        const val RESULT_INSERT = 1
        const val RESULT_UPDATE = 2
        const val EXTRA_CONTACT_ID = "pl.bgn.roompoc.ID"
    }
}
