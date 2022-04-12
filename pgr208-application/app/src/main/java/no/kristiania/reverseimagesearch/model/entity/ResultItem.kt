package no.kristiania.reverseimagesearch.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultItem(
    val url: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "result_item_id")
    val id: Long? = null
)



