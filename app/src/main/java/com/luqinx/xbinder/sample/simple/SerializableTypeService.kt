package com.luqinx.xbinder.sample.simple

import android.os.Parcelable
import com.luqinx.xbinder.ILightBinder
import java.io.Serializable

/**
 * @author  qinchao
 *
 * @since 2022/3/25
 */
interface SerializableTypeService: ILightBinder {
    fun getSerializable(): Serializable
    fun getSerializableArray(): Array<Serializable>
    fun getSerializableList(): List<Serializable>
    fun setSerializable(value: Serializable)
    fun setSerializableArray(values: Array<Serializable>)
    fun setSerializableList(values: List<Serializable>)
}