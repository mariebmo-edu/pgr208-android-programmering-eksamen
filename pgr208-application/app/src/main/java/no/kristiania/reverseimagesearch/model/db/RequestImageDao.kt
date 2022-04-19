package no.kristiania.reverseimagesearch.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import no.kristiania.reverseimagesearch.model.entity.RequestImage

@Dao
interface RequestImageDao {
    @Insert
    suspend fun insert(requestImage: RequestImage): Long

    @Update
    suspend fun update(requestImage: RequestImage)

    @Query("SELECT * FROM request_image_table ORDER BY request_image_id DESC")
    fun getAll(): LiveData<List<RequestImage>>

}