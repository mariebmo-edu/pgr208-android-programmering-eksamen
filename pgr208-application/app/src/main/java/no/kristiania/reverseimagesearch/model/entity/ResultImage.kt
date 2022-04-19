package no.kristiania.reverseimagesearch.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "result_image_table",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = RequestImage::class,
            parentColumns = arrayOf("request_image_id"),
            childColumns = arrayOf("request_image_id"), onDelete = ForeignKey.CASCADE
        )
    )
)
data class ResultImage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "result_item_id")
    val id: Long? = null,
    @ColumnInfo(name = "result_item_server_path")
    val serverPath: String? = null,
    @ColumnInfo(name = "result_item_data")
    var data: ByteArray? = null,
    @ColumnInfo(name = "request_image_id", index = true)
    var requestImageId: Long? = null
)



