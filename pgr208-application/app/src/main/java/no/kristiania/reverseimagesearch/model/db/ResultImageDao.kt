package no.kristiania.reverseimagesearch.model.db

import androidx.room.*
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage

@Dao
interface ResultImageDao {
    @Insert
    suspend fun insert(resultImage: ResultImage)

    @Update
    suspend fun update(resultImage: ResultImage)

    @Delete
    suspend fun delete(resultImage: ResultImage)

    @Query("SELECT * FROM request_image_table AS REQ " +
            "JOIN result_image_table AS RES " +
            "ON RES.request_image_id = REQ.request_image_id")
    suspend fun getSearchResultById(): Map<RequestImage,List<ResultImage>>
}