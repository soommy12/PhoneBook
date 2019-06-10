package pl.bgn.roompoc.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import pl.bgn.roompoc.data.Word
import pl.bgn.roompoc.data.WordDao

class WordRepository(private val wordDao: WordDao) {

    val allWords: LiveData<List<Word>> = wordDao.getAllWord()

    @WorkerThread
    suspend fun insert(word: Word){
        wordDao.insert(word)
    }


}