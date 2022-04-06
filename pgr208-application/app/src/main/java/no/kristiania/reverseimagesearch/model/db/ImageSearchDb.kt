package no.kristiania.reverseimagesearch.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import no.kristiania.reverseimagesearch.model.entity.RequestImage

@Database(entities = [RequestImage::class], version = 1, exportSchema = false)
abstract class ImageSearchDb : RoomDatabase() {
    abstract val requestImageDao: RequestImageDao

    companion object {
        @Volatile
        private var INSTANCE: ImageSearchDb? = null

        fun getInstance(context: Context): ImageSearchDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ImageSearchDb::class.java,
                        "image_search_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}