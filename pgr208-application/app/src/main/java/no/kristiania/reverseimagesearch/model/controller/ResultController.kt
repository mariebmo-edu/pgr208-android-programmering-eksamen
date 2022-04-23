package no.kristiania.reverseimagesearch.model.controller

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import java.lang.Exception

class ResultController(
    private val resultImageDao: ResultImageDao,
    private val requestImageDao: RequestImageDao
) {

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

    suspend fun getByParentId(requestImgId: Long) = CoroutineScope(Dispatchers.IO).async {
        return@async resultImageDao.getByParentId(requestImgId)
    }.await()
}
