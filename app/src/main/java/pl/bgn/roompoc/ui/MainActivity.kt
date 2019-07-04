package pl.bgn.roompoc.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import pl.bgn.roompoc.adapters.ContactListAdapter
import pl.bgn.roompoc.R
import pl.bgn.roompoc.viewmodel.ContactsViewModel

class MainActivity : AppCompatActivity(), ContactListAdapter.OnContactListener {


    override fun onContactClick(position: Int) {
        val intent = Intent(this@MainActivity, SingleContactActivity::class.java)
        intent.putExtra(SingleContactActivity.EXTRA_CONTACT_ID, contactsViewModel.contacts.value!![position].id)
        startActivityForResult(intent, newWordActivityRequestCode)
    }

    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var searchView: SearchView
    private lateinit var adapter: ContactListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerview)
        adapter = ContactListAdapter(this)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = contactsViewModel.contacts.value!![position]
                contactsViewModel.delete(contact)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        contactsViewModel.contacts.observe(this, Observer { contacts ->
            contacts?.let {
                adapter.setContacts(it)
            }
        })

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, SingleContactActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    private val onQuerySearchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            getContactsFromDB(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            getContactsFromDB(newText)
            return true
        }

        private fun getContactsFromDB(searchText: String?) {
            val search = "%$searchText%"
            contactsViewModel.setSearchValue(search)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_search)
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                searchView.setQuery("", false) // to wykona nam onQuerySearch listener wiec w konsekwencji vieModel.setSearchValue
                return true
            }
        })
        searchView = menuItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(onQuerySearchListener)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == newWordActivityRequestCode){
            when (resultCode) {
                SingleContactActivity.RESULT_INSERT -> Toast.makeText(this, "New contact added", Toast.LENGTH_SHORT).show()
                SingleContactActivity.RESULT_UPDATE -> Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show()
                SingleContactActivity.RESULT_NO_DATA -> Toast.makeText(applicationContext,
                    R.string.empty_not_saved, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val newWordActivityRequestCode = 1
    }
}
