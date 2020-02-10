package pl.bgn.roompoc.db

import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bgn.roompoc.db.dao.AddressDao
import pl.bgn.roompoc.db.dao.BuildingDao
import pl.bgn.roompoc.db.dao.CompanyDao
import pl.bgn.roompoc.db.dao.ContactDao
import pl.bgn.roompoc.db.entity.Address
import pl.bgn.roompoc.db.entity.Building
import pl.bgn.roompoc.db.entity.Company
import pl.bgn.roompoc.db.entity.Contact
import javax.security.auth.login.LoginException
import androidx.sqlite.db.SupportSQLiteDatabase as SupportSQLiteDatabase1

@Database(entities = [Contact::class, Address::class, Building::class, Company::class], version = 5)
@TypeConverters(Converters::class)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun contactDao() : ContactDao
    abstract fun addressDao() : AddressDao
    abstract fun companyDao() : CompanyDao
    abstract fun buildingDao() : BuildingDao

        companion object {

//            private val MIGRATION_1_2 = object : Migration(1,2) {
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    database.execSQL("CREATE TABLE 'contact_table' ('id' INTEGER NOT NULL, 'surname' TEXT NOT NULL, 'name' TEXT NOT NULL, 'number' INTEGER NOT NULL, PRIMARY KEY('id'))")
//                }
//            }
//
//            private val MIGRATION_2_3 = object : Migration(2,3){
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    database.execSQL("DROP TABLE 'word_table'")
//                }
//            }
//
//            private val MIGRATION_3_4 = object :Migration(3,4){
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    database.execSQL("CREATE TABLE 'address_table' ('id' INTEGER NOT NULL, 'contactId' INTEGER NOT NULL, 'city' TEXT NOT NULL, 'street' TEXT NOT NULL, 'number' INTEGER NOT NULL, PRIMARY KEY('id'), FOREIGN KEY ('contactId') REFERENCES Contact('id'))")
//                }
//            }
//
//            private val MIGRATION_4_5 = object : Migration(4,5){
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    database.execSQL("CREATE TABLE IF NOT EXISTS 'building_table' ('id' INTEGER NOT NULL, 'companyId' INTEGER NOT NULL, 'city' TEXT NOT NULL, 'street' TEXT NOT NULL, PRIMARY KEY('id'), FOREIGN KEY('companyId') REFERENCES Company('id') ON UPDATE NO ACTION ON DELETE CASCADE)")
//                    database.execSQL("CREATE TABLE IF NOT EXISTS 'company_table' ('id' INTEGER NOT NULL, 'name' TEXT NOT NULL, PRIMARY KEY('id'))")
//                }
//            }

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
//                        .fallbackToDestructiveMigration() // if no migrations, or migrations are wrong - destroy whole db
//                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class ContactDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase1) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.companyDao(), database.buildingDao())
                        val cursor: Cursor = database.contactDao().getContactWithId()
                        Log.e("TEST", cursor.columnNames[0].toString())
                        val map = mutableMapOf<String, Int>()
                        if(cursor.moveToFirst()){
                            do {
                                val name = cursor.getString(cursor.getColumnIndex("name"))
                                val id = cursor.getInt(cursor.getColumnIndex("id"))
                                map[name] = id
                            } while (cursor.moveToNext())
                        }
                        Log.e("TEST", "MAP: $map")
                    }
            }
        }

        suspend fun populateDatabase(companyDao: CompanyDao, buildingDao: BuildingDao) {
            companyDao.insert(Company(0, "SuperCompany"))
//            companyDao.insert(Company(0,1, "Spini"))
//            companyDao.insert(Company(0,2, "Nike"))
            buildingDao.insert(Building(0, 0, "Rzeszow", "Pierwsza"))
            buildingDao.insert(Building(0, 1, "Krakow", "Druga"))
            buildingDao.insert(Building(0, 2, "Warszawa", "Trzecia"))
        }
    }


}