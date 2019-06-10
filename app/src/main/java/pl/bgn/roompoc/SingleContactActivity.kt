package pl.bgn.roompoc

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class SingleContactActivity : AppCompatActivity() {

    private lateinit var editNameView: EditText
    private lateinit var editSurnameView: EditText
    private lateinit var editPhoneNumberView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_single)
        editNameView = findViewById(R.id.edit_name)
        editSurnameView = findViewById(R.id.edit_surname)
        editPhoneNumberView = findViewById(R.id.edit_phone)

        val bundle : Bundle ?= intent.extras
        editNameView.setText(bundle?.getString(EXTRA_NAME))
        editSurnameView.setText(bundle?.getString(EXTRA_SURNAME))
        if(bundle?.getInt(EXTRA_PHONE) != null)
            editPhoneNumberView.setText(bundle.getInt(EXTRA_PHONE).toString())
        val id = bundle?.getInt(EXTRA_ID)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editNameView.text) || TextUtils.isEmpty(editSurnameView.text)
                || TextUtils.isEmpty(editPhoneNumberView.text)) {
                setResult(RESULT_NO_DATA, replyIntent)
            } else {
                val name = editNameView.text.toString()
                val surname = editSurnameView.text.toString()
                val phone : Int = editPhoneNumberView.text.toString().toInt()
                replyIntent.putExtra(EXTRA_NAME, name)
                replyIntent.putExtra(EXTRA_SURNAME, surname)
                replyIntent.putExtra(EXTRA_PHONE, phone)
                if(id != null) {
                    replyIntent.putExtra(EXTRA_ID, id)
                    setResult(RESULT_UPDATE, replyIntent)
                } else setResult(RESULT_INSERT, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val RESULT_NO_DATA = -2
        const val RESULT_INSERT = 1
        const val RESULT_UPDATE = 2
        const val EXTRA_ID = "pl.bgn.roompoc.ID"
        const val EXTRA_NAME = "pl.bgn.roompoc.NAME"
        const val EXTRA_SURNAME = "pl.bgn.roompoc.SURNANE"
        const val EXTRA_PHONE = "pl.bgn.roompoc.PHONE"
    }
}
