package com.luqinx.xbinder.sample.simple

/**
 * @author  qinchao
 *
 * @since 2022/3/25
 */
interface ParcelableTypeService {
    fun getParcelable(): ParcelableData?
    fun getParcelableArray(): Array<ParcelableData>?
    fun getParcelableList(): List<ParcelableData>?
    fun setParcelable(value: ParcelableData?)
    fun setParcelableArray(values: Array<ParcelableData>?)
    fun setParcelableList(values: List<ParcelableData>?)
}