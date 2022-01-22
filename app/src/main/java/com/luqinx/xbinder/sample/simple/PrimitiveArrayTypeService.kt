package com.luqinx.xbinder.sample.simple

import com.luqinx.xbinder.IBinderService


/**
 * @author  qinchao
 *
 * @since 2022/1/8
 */
interface PrimitiveArrayTypeService: IBinderService {
    fun getCharArray(): CharArray?

    fun getByteArray(): ByteArray?

    fun getShortArray(): ShortArray?

    fun getIntArray(): IntArray?

    fun getLongArray(): LongArray?

    fun getFloatArray(): FloatArray?

    fun getDoubleArray(): DoubleArray?

    fun getBooleanArray(): BooleanArray?

    fun getNull(): IntArray?

    fun setCharArray(c: CharArray)

    fun setByteArray(b: ByteArray)

    fun setShortArray(s: ShortArray)

    fun setIntArray(i: IntArray)

    fun setLongArray(l: LongArray)

    fun setFloatArray(f: FloatArray)

    fun setDoubleArray(d: DoubleArray)

    fun setBooleanArray(b: BooleanArray)

    fun setNull(s: IntArray?)

    fun setMultiParameters(i: IntArray, f: FloatArray, d: DoubleArray)
}