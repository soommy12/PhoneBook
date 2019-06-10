package pl.bgn.roompoc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Word::class, Contact::class], version = 2)
public abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao() : WordDao
    abstract fun contactDao() : ContactDao



        companion object {

            private val MIGRATION_1_2 = object : Migration(1,2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE 'contact_table' ('id' INTEGER NOT NULL, 'surname' TEXT NOT NULL, 'name' TEXT NOT NULL, 'number' INTEGER NOT NULL, PRIMARY KEY('id'))")
                }
            }

            @Volatile
            private var INSTANCE: WordRoomDatabase? = null
            fun getDatabase(
                context : Context,
                scope: CoroutineScope): WordRoomDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "word_database")
//                    .addCallback(WordRoomCallback(scope))
//                        .fallbackToDestructiveMigration() // good for testing, but this clear whole data
                        .addMigrations(MIGRATION_1_2)
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class WordRoomCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            wordDao.deleteAll()
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World")
            wordDao.insert(word)

        }
    }


}