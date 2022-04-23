package no.kristiania.reverseimagesearch.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage

@Dao
interface ResultImageDao {
    @Insert
    suspend fun insert(resultImage: ResultImage): Long

    @Insert
    suspend fun insertMany(resultImages: List<ResultImage>)

    @Update
    suspend fun update(resultImage: ResultImage)

    @Delete
    suspend fun delete(resultImage: ResultImage)

    @Query("SELECT * FROM request_image_table AS REQ " +
            "JOIN result_image_table AS RES " +
            "ON RES.request_image_id = REQ.request_image_id")
    suspend fun getSearchResultById(): Map<RequestImage,List<ResultImage>>

    @Query("SELECT * FROM result_image_table WHERE request_image_id = :id ORDER BY result_item_id DESC")
    fun getByParentId(id: Long): LiveData<List<ResultImage>>
}