package no.kristiania.reverseimagesearch.model.db

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.room.*
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage


// App.req. 4
@Dao
interface ResultImageDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    suspend fun insert(resultImage: ResultImage): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    suspend fun insertMany(resultImages: List<ResultImage>)

    @Update
    @Throws(SQLiteException::class)

    suspend fun update(resultImage: ResultImage)

    @Delete
    @Throws(SQLiteException::class)
    suspend fun delete(resultImage: ResultImage)

    @Query(
        "SELECT * FROM request_image_table AS REQ " +
                "JOIN result_image_table AS RES " +
                "ON RES.request_image_id = REQ.request_image_id"
    )
    @Throws(SQLiteException::class)
    suspend fun getSearchResultById(): Map<RequestImage, List<ResultImage>>

    @Query("SELECT * FROM result_image_table WHERE request_image_id = :id ORDER BY result_item_id DESC")
    @Throws(SQLiteException::class)
    fun getByParentId(id: Long): LiveData<List<ResultImage>>
}