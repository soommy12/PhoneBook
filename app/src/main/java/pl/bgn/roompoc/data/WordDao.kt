package pl.bgn.roompoc.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDao {

    @Insert
    suspend fun insert(word : Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAllWord() : LiveData<List<Word>>
}
