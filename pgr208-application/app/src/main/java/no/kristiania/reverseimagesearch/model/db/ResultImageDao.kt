package no.kristiania.reverseimagesearch.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import no.kristiania.reverseimagesearch.model.entity.ResultImage

@Dao
interface ResultImageDao {
    @Insert
    suspend fun insert(resultImage: ResultImage)

    @Update
    suspend fun update(resultImage: ResultImage)

    @Delete
    suspend fun delete(resultImage: ResultImage)
}