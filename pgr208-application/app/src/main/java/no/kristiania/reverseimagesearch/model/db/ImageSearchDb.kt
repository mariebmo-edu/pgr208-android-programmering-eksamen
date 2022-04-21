package no.kristiania.reverseimagesearch.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage

@Database(entities = [RequestImage::class, ResultImage::class], version = 3, exportSchema = false)
abstract class ImageSearchDb : RoomDatabase() {
    abstract val requestImageDao: RequestImageDao
    abstract val resultImageDao: ResultImageDao

    companion object {
        @Volatile
        private var INSTANCE: ImageSearchDb? = null
// TODO: Bør nok fjerne fallback destructive greia
        fun getInstance(context: Context): ImageSearchDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ImageSearchDb::class.java,
                        "image_search_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}