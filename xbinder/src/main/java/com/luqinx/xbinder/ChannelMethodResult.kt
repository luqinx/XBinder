package com.luqinx.xbinder

import android.os.Parcel
import android.os.Parcelable

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
class ChannelMethodResult() : Parcelable {

    var succeed: Boolean = false

    var errMessage: String? = null

    var value: Any? = null

    var invokeConsumer = 0L

    constructor(parcel: Parcel) : this() {
        succeed = parcel.readInt() == 1
        errMessage = parcel.readString()
        value = parcel.readValue(options.classLoader)
        invokeConsumer = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt( if (succeed) 1 else 0 )
        parcel.writeString(errMessage)
        parcel.writeValue(value)
        parcel.writeLong(invokeConsumer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChannelMethodResult> {
        override fun createFromParcel(parcel: Parcel): ChannelMethodResult {
            return ChannelMethodResult(parcel)
        }

        override fun newArray(size: Int): Array<ChannelMethodResult?> {
            return arrayOfNulls(size)
        }
    }
}