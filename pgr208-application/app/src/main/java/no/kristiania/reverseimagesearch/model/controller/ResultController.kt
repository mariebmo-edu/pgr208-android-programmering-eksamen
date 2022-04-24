package no.kristiania.reverseimagesearch.model.controller

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage

class ResultController(
    private val resultImageDao: ResultImageDao,
    private val requestImageDao: RequestImageDao
) {
    @Throws(SQLiteException::class)
    fun saveAll(
        requestImage: RequestImage,
        imagesToSave: List<ResultImage>
    ) = CoroutineScope(Dispatchers.IO).async {

        val reqSave = async { requestImageDao.insert(requestImage) }
        val reqImgId = reqSave.await()
        imagesToSave.forEach {
            it.requestImageId = reqImgId
        }
        resultImageDao.insertMany(imagesToSave)
    }

    suspend fun getByParentId(requestImgId: Long): LiveData<List<ResultImage>> {

        fun getFromDb() = CoroutineScope(Dispatchers.IO).async {
            return@async resultImageDao.getByParentId(requestImgId)
        }

        return getFromDb().await()
    }
}
