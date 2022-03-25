package com.luqinx.xbinder.sample.simple

import android.os.Parcel
import android.os.Parcelable

class ParcelableData() : Parcelable {
        var s: String? = null
        var i: Int = 0
        var f: Float = 0f
        
        constructor(parcel: Parcel) : this() {
            s = parcel.readString()
            i = parcel.readInt()
            f = parcel.readFloat()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(s)
            parcel.writeInt(i)
            parcel.writeFloat(f)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ParcelableData

            if (s != other.s) return false
            if (i != other.i) return false
            if (f != other.f) return false

            return true
        }

        override fun hashCode(): Int {
            var result = s?.hashCode() ?: 0
            result = 31 * result + i
            result = 31 * result + f.hashCode()
            return result
        }


        companion object CREATOR : Parcelable.Creator<ParcelableData> {
            override fun createFromParcel(parcel: Parcel): ParcelableData {
                return ParcelableData(parcel)
            }

            override fun newArray(size: Int): Array<ParcelableData?> {
                return arrayOfNulls(size)
            }
        }

    }