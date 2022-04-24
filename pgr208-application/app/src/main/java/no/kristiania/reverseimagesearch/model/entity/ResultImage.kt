package no.kristiania.reverseimagesearch.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
// App.req. 4
@Entity(
    tableName = "result_image_table",
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
    // Sub req. 8
    @ColumnInfo(name = "result_item_data", typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray? = null,
    @ColumnInfo(name = "request_image_id", index = true)
    var requestImageId: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResultImage

        if (id != other.id) return false
        if (serverPath != other.serverPath) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false
        if (requestImageId != other.requestImageId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (serverPath?.hashCode() ?: 0)
        result = 31 * result + (data?.contentHashCode() ?: 0)
        result = 31 * result + (requestImageId?.hashCode() ?: 0)
        return result
    }
}



