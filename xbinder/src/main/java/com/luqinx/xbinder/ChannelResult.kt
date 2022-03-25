package com.luqinx.xbinder

import android.os.Parcel
import android.os.Parcelable
import com.luqinx.xbinder.serialize.GenericAdapter
import com.luqinx.xbinder.serialize.ObjectAdapter
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
class ChannelResult() : Parcelable {

    var succeed: Boolean = false

    var errMessage: String? = null

    var errCode: Int = BinderInvoker.ERROR_CODE_SUCCESS

    var returnValue: Any? = null

    lateinit var returnType: Type

    var invokeConsumer = 0L

    constructor(parcel: Parcel) : this() {
        succeed = parcel.readInt() == 1
        errMessage = parcel.readString()
        invokeConsumer = parcel.readLong()
        if (parcel.readInt() > 0) {
            returnType = GenericAdapter.readInstance(parcel, Any::class.java)!!
            returnValue = ObjectAdapter.read(parcel, returnType)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt( if (succeed) 1 else 0 )
        parcel.writeString(errMessage)
        parcel.writeLong(invokeConsumer)
        if (returnValue != null) {
            parcel.writeInt(1)
            GenericAdapter.writeInstance(parcel, returnType, returnType)
            ObjectAdapter.write(parcel, returnValue, returnType)
        } else {
            parcel.writeInt(-1)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChannelResult> {
        override fun createFromParcel(parcel: Parcel): ChannelResult {
            return ChannelResult(parcel)
        }

        override fun newArray(size: Int): Array<ChannelResult?> {
            return arrayOfNulls(size)
        }
    }
}