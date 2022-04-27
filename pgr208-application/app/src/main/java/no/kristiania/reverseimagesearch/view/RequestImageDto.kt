package no.kristiania.reverseimagesearch.view

import android.os.Parcel
import android.os.Parcelable

class RequestImageDto : Parcelable {

    var url: String?
        public set
    var uri: String?
        public set

    constructor(url: String?, uri: String?) {
        this.url = url
        this.uri = uri
    }

    constructor(parcel: Parcel) : this(
        url = parcel.readString() ?: "",
        uri = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(uri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RequestImageDto> {
        override fun createFromParcel(parcel: Parcel): RequestImageDto {
            return RequestImageDto(parcel)
        }

        override fun newArray(size: Int): Array<RequestImageDto?> {
            return arrayOfNulls(size)
        }
    }
}