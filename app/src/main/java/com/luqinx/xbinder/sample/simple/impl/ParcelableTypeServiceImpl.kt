package com.luqinx.xbinder.sample.simple.impl

import com.luqinx.xbinder.sample.simple.ParcelableData
import com.luqinx.xbinder.sample.simple.ParcelableTypeService


/**
 * @author  qinchao
 *
 * @since 2022/3/25
 */
class ParcelableTypeServiceImpl: ParcelableTypeService {
    
    private var data: ParcelableData? = null
    
    private var dataArray: Array<ParcelableData>? = null
    
    private var dataList: List<ParcelableData>? = null
    
    override fun getParcelable(): ParcelableData? {
        return data
    }

    override fun getParcelableArray(): Array<ParcelableData>? {
        return dataArray
    }

    override fun getParcelableList(): List<ParcelableData>? {
        return dataList
    }

    override fun setParcelable(value: ParcelableData?) {
        this.data = value
    }

    override fun setParcelableArray(values: Array<ParcelableData>?) {
        this.dataArray = values
    }

    override fun setParcelableList(values: List<ParcelableData>?) {
        this.dataList = values
    }
}