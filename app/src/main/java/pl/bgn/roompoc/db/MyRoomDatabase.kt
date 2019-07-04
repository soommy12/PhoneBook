package pl.bgn.roompoc.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.db.dao.AddressDao
import pl.bgn.roompoc.db.dao.ContactDao
import pl.bgn.roompoc.db.entity.Address
import pl.bgn.roompoc.db.entity.Contact

@Database(entities = [Contact::class, Address::class], version = 4)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun contactDao() : ContactDao
    abstract fun addressDao() : AddressDao

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

            private val MIGRATION_3_4 = object :Migration(3,4){
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE 'address_table' ('id' INTEGER NOT NULL, 'contactId' INTEGER NOT NULL, 'city' TEXT NOT NULL, 'street' TEXT NOT NULL, 'number' INTEGER NOT NULL, PRIMARY KEY('id'), FOREIGN KEY ('contactId') REFERENCES Contact('id'))")
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
                        "contact_database")
                        .addCallback(ContactDatabaseCallback(scope))
                        .fallbackToDestructiveMigration() // if no migrations, or migrations are wrong - destroy whole db
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class ContactDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

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
        }
    }


}