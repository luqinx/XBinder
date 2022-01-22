package com.luqinx.xbinder

import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class ParcelableBinder : Parcelable {
    private val mBinder: IBinder

    constructor(`in`: Parcel) {
        mBinder = `in`.readStrongBinder()
    }

    constructor(binder: IBinder) {
        this.mBinder = binder
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: IBinder> getBinder(): T {
        return mBinder as T
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStrongBinder(mBinder)
    }

    companion object CREATOR : Creator<ParcelableBinder> {
        override fun createFromParcel(parcel: Parcel): ParcelableBinder {
            return ParcelableBinder(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableBinder?> {
            return arrayOfNulls(size)
        }
    }

}