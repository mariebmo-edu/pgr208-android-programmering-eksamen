package no.kristiania.reverseimagesearch.model.entity

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.io.IOException

@Entity(tableName = "request_image_table")
data class RequestImage(
    @ColumnInfo(name = "request_image_path")
    private val path: String,

    @ColumnInfo(name = "request_image_id")
    @PrimaryKey(autoGenerate = true)
    private val id: Long? = null,

    @ColumnInfo(name = "request_image_server_path")
    private val serverPath: String? = null,
) {
    @ColumnInfo(name = "request_image_file")
    private var _image: File? = null
    val image: File?
        get() = _image

    init {
        try {
            _image = File(path)
        } catch (e: IOException) {
            Log.e("Error reading file", e.message.toString())
        }
    }
}