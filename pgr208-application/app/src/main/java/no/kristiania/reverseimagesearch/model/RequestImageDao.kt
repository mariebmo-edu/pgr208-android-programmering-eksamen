package no.kristiania.reverseimagesearch.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RequestImageDao {
    @Insert
    suspend fun insert(requestImage: RequestImage)

    @Update
    suspend fun update(requestImage: RequestImage)

    @Query("SELECT * FROM request_image_table ORDER BY request_image_id DESC")
    fun getAll(): LiveData<List<RequestImage>>
}