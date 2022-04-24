package no.kristiania.reverseimagesearch.model.db

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.room.*
import no.kristiania.reverseimagesearch.model.entity.RequestImage


// App.req. 4
@Dao
interface RequestImageDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    suspend fun insert(requestImage: RequestImage): Long

    @Update(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    suspend fun update(requestImage: RequestImage)

    @Query("SELECT * FROM request_image_table ORDER BY request_image_id DESC")
    @Throws(SQLiteException::class)
    fun getAll(): LiveData<List<RequestImage>>

}