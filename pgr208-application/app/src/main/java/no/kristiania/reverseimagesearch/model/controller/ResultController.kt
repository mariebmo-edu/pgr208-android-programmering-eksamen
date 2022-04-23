package no.kristiania.reverseimagesearch.model.controller

import android.accounts.NetworkErrorException
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import java.lang.Exception

class ResultController(private val resultImageDao: ResultImageDao,private val requestImageDao: RequestImageDao) {
    fun saveAll(requestImage: RequestImage, imagesToSave: List<ResultImage>, collectionName: String) {

        runBlocking {
            launch(Dispatchers.IO) {
                val reqSave = async {requestImageDao.insert(requestImage)}
                val reqImgId = reqSave.await()

                imagesToSave.forEach {
                    it.requestImageId = reqImgId
                }
                resultImageDao.insertMany(imagesToSave)
            }
        }

    }
}