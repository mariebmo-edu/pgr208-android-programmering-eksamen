package no.kristiania.reverseimagesearch.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request_image_table")
data class RequestImage(
    @ColumnInfo(name = "request_image_id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "request_image_server_path")
    var serverPath: String? = null,

    @ColumnInfo(name = "request_image_data",typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray? = null,

    @ColumnInfo(name = "request_image_collection_name")
    var collectionName : String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RequestImage

        if (id != other.id) return false
        if (serverPath != other.serverPath) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (serverPath?.hashCode() ?: 0)
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }
}