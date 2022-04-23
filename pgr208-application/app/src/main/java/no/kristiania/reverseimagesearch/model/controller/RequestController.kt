package no.kristiania.reverseimagesearch.model.controller

import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage

class RequestController(private val requestImageDao: RequestImageDao) {
    suspend fun insert(requestImage: RequestImage): Long {

        fun insertIntoDb(requestImage: RequestImage) = CoroutineScope(Dispatchers.IO).async {
            return@async requestImageDao.insert(requestImage)
        }

        return insertIntoDb(requestImage).await()
    }
}