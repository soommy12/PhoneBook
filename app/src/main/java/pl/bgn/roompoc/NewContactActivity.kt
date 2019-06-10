package pl.bgn.roompoc

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewContactActivity : AppCompatActivity() {

    private lateinit var editNameView: EditText
    private lateinit var editSurnameView: EditText
    private lateinit var editPhoneNumberView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        editNameView = findViewById(R.id.edit_name)
        editSurnameView = findViewById(R.id.edit_surname)
        editPhoneNumberView = findViewById(R.id.edit_phone)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editNameView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val name = editNameView.text.toString()
                val surname = editSurnameView.text.toString()
                val phone = editPhoneNumberView.text.toString()
                replyIntent.putExtra(EXTRA_NAME, name)
                replyIntent.putExtra(EXTRA_SURNAME, surname)
                replyIntent.putExtra(EXTRA_PHONE, surname)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_UPDATE = "pl.bgn.roompoc.UPDATE"
        const val EXTRA_INSERT = "pl.bgn.roompoc.INSERT"
        const val EXTRA_REPLY = "pl.bgn.roompoc.REPLY"
        const val EXTRA_NAME = "pl.bgn.roompoc.NAME"
        const val EXTRA_SURNAME = "pl.bgn.roompoc.SURNANE"
        const val EXTRA_PHONE = "pl.bgn.roompoc.PHONE"
    }
}
