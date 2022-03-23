package com.luqinx.xbinder.sample.simple

import com.luqinx.xbinder.ILightBinder


/**
 * @author  qinchao
 *
 * @since 2022/1/8
 */
interface PrimitiveBoxArrayTypeService: ILightBinder {
    fun getCharArray(): Array<Char>?

    fun getByteArray(): Array<Byte>?

    fun getShortArray(): Array<Short>?

    fun getIntArray(): Array<Int>?

    fun getLongArray(): Array<Long>?

    fun getFloatArray(): Array<Float>?

    fun getDoubleArray(): Array<Double>?

    fun getBooleanArray(): Array<Boolean>?

    fun getStringArray(): Array<String?>?

    fun getNull(): Array<String?>?

    fun setCharArray(c: Array<Char>)

    fun setByteArray(b: Array<Byte>)

    fun setShortArray(s: Array<Short>)

    fun setIntArray(i: Array<Int>)

    fun setLongArray(l: Array<Long>)

    fun setFloatArray(f: Array<Float>)

    fun setDoubleArray(d: Array<Double>)

    fun setBooleanArray(b: Array<Boolean>)

    fun setStringArray(s: Array<String?>)

    fun setNull(s: Array<Int?>?)

    fun setMultiParameters(i: Array<Int>, s: Array<Float>, f: Array<Double>)
}