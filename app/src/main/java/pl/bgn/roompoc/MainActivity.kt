package pl.bgn.roompoc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import pl.bgn.roompoc.data.Contact

class MainActivity : AppCompatActivity(), ContactListAdapter.OnContactListener {


    override fun onContactClick(position: Int) {
        val intent = Intent(this@MainActivity, SingleContactActivity::class.java)
        val contact = contactViewModel.allContacts.value!![position]
        intent.putExtra(SingleContactActivity.EXTRA_ID, contact.id)
        intent.putExtra(SingleContactActivity.EXTRA_NAME, contact.name)
        intent.putExtra(SingleContactActivity.EXTRA_SURNAME, contact.surname)
        intent.putExtra(SingleContactActivity.EXTRA_PHONE, contact.number)
        startActivityForResult(intent, newWordActivityRequestCode)
    }

    private lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ContactListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = contactViewModel.allContacts.value!![position]
                contactViewModel.delete(contact)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        contactViewModel.allContacts.observe(this, Observer { contacts ->
            contacts?.let { adapter.setContacts(it) }
        })

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, SingleContactActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == newWordActivityRequestCode){
            when (resultCode) {
                SingleContactActivity.RESULT_INSERT -> data?.let{
                    val contact = Contact(
                        0,
                        it.getStringExtra(SingleContactActivity.EXTRA_NAME),
                        it.getStringExtra(SingleContactActivity.EXTRA_SURNAME),
                        it.getIntExtra(SingleContactActivity.EXTRA_PHONE, 0))
                    contactViewModel.insert(contact)
                }
                SingleContactActivity.RESULT_UPDATE -> data?.let{
                    val contact = Contact(
                        it.getIntExtra(SingleContactActivity.EXTRA_ID, 0),
                        it.getStringExtra(SingleContactActivity.EXTRA_NAME),
                        it.getStringExtra(SingleContactActivity.EXTRA_SURNAME),
                        it.getIntExtra(SingleContactActivity.EXTRA_PHONE, 0))
                    contactViewModel.update(contact)
                }
                SingleContactActivity.RESULT_NO_DATA -> Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val newWordActivityRequestCode = 1
    }
}
