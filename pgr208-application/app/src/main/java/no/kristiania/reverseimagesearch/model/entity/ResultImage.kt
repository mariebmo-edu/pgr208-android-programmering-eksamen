package no.kristiania.reverseimagesearch.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultImage (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "result_item_id")
    val id: Long? = null,
    @ColumnInfo(name = "result_item_server_path")
    val serverPath: String? = null,
    @ColumnInfo(name = "result_item_data")
    val data: ByteArray? = null
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

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (serverPath?.hashCode() ?: 0)
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }
}



