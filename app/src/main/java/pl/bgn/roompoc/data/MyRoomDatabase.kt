package pl.bgn.roompoc.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.SingleContactViewModel
import java.util.*

@Database(entities = [Contact::class], version = 3)
abstract class MyRoomDatabase : RoomDatabase() {


    abstract fun contactDao() : ContactDao

        companion object {

            private val MIGRATION_1_2 = object : Migration(1,2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE 'contact_table' ('id' INTEGER NOT NULL, 'surname' TEXT NOT NULL, 'name' TEXT NOT NULL, 'number' INTEGER NOT NULL, PRIMARY KEY('id'))")
                }
            }

            private val MIGRATION_2_3 = object : Migration(2,3){
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("DROP TABLE 'word_table'")
                }
            }

            @Volatile
            private var INSTANCE: MyRoomDatabase? = null
            fun getDatabase(
                context : Context,
                scope: CoroutineScope): MyRoomDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyRoomDatabase::class.java,
                        "word_database")
                        .addCallback(ContactDatabaseCallback(scope))
                        .fallbackToDestructiveMigration() // if no migrations, or migrations are wrong - destroy whole db
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class ContactDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        val names = listOf("Jacek", "Gnacek", "Wiesiek", "Czesieg", "Ignacy", "DoTacy", "Tyrion", "Furion", "Bartosz", "Janina")
        val surnames = listOf("Gerwazy", "Lotto", "Waszny", "Graszny", "Toczy", "Maszyny", "Amator", "Torano", "Tradys", "Marszal")
        val numbers = listOf(143,543,312,548,9454,4723,432,4328,135,432)

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.contactDao())
                    }
            }
        }

        suspend fun populateDatabase(contactDao: ContactDao) {
           // mozna dac jakies inserty tutaj

//            Log.e("INFO", "Putting 10k elements into DB...")
//            for(i in 0..10000){
//                val Contact = Contact(
//                    0,
//                    surnames[(0..9).random()],
//                    names[(0..9).random()],
//                    numbers[(0..9).random()]
//                )
//                contactDao.insert(Contact)
//            }
//            Log.e("INFO", "Done")

        }
    }


}