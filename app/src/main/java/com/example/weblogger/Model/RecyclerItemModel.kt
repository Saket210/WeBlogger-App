package com.example.weblogger.Model

import android.os.Parcel
import android.os.Parcelable

data class RecyclerItemModel(
    var title: String? = "null",
    val profileUrl: String? = "null",
    val bloggerName: String? = "null",
    var userId:String? = "null",
    val date: String? = "null",
    var blogContent: String? = "null",
    var likeCount: Int = 0,
    var isSaved:Boolean = false,
    var blogPostId: String? = "null",
    val likedBy: MutableList<String>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?: "null",
        parcel.readString()?: "null",
        parcel.readString()?: "null",
        parcel.readString()?: " null",
        parcel.readString()?: "null",
        parcel.readString()?: "null",
        parcel.readInt(),
        parcel.readByte()!=0.toByte(),
        parcel.readString()?: "null"
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(profileUrl)
        parcel.writeString(bloggerName)
        parcel.writeString(userId)
        parcel.writeString(date)
        parcel.writeString(blogContent)
        parcel.writeInt(likeCount)
        parcel.writeByte(if (isSaved) 1 else 0)
        parcel.writeString(blogPostId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecyclerItemModel> {
        override fun createFromParcel(parcel: Parcel): RecyclerItemModel {
            return RecyclerItemModel(parcel)
        }

        override fun newArray(size: Int): Array<RecyclerItemModel?> {
            return arrayOfNulls(size)
        }
    }

}
