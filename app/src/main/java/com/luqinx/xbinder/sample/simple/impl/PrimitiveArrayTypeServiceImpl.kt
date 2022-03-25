package com.luqinx.xbinder.sample.simple.impl

import com.luqinx.xbinder.sample.BuildConfig
import com.luqinx.xbinder.sample.simple.PrimitiveArrayTypeService

/**
 * @author  qinchao
 *
 * @since 2022/1/8
 */
class PrimitiveArrayTypeServiceImpl: PrimitiveArrayTypeService {

    private var charArray: CharArray? = null
    private var byteArray: ByteArray? = null
    private var shortArray: ShortArray? = null
    private var intArray: IntArray? = null
    private var longArray: LongArray? = null
    private var floatArray: FloatArray? = null
    private var doubleArray: DoubleArray? = null
    private var booleanArray: BooleanArray? = null

    override fun getCharArray(): CharArray? {
        return charArray
    }

    override fun getByteArray(): ByteArray? {
        return byteArray
    }

    override fun getShortArray(): ShortArray? {
        return shortArray
    }

    override fun getIntArray(): IntArray? {
        return intArray
    }

    override fun getLongArray(): LongArray? {
        return longArray
    }

    override fun getFloatArray(): FloatArray? {
        return floatArray
    }

    override fun getDoubleArray(): DoubleArray? {
        return doubleArray
    }

    override fun getBooleanArray(): BooleanArray? {
        return booleanArray
    }

    override fun getNull(): IntArray? {
        return null
    }

    override fun setCharArray(c: CharArray) {
        charArray = c
    }

    override fun setByteArray(b: ByteArray) {
        byteArray = b
    }

    override fun setShortArray(s: ShortArray) {
        shortArray = s
    }

    override fun setIntArray(i: IntArray) {
        intArray = i
    }

    override fun setLongArray(l: LongArray) {
        longArray = l
    }

    override fun setFloatArray(f: FloatArray) {
        floatArray = f
    }

    override fun setDoubleArray(d: DoubleArray) {
        doubleArray = d
    }

    override fun setBooleanArray(b: BooleanArray) {
        booleanArray = b
    }

    override fun setNull(s: IntArray?) {
        if (BuildConfig.DEBUG && s != null) {
            error("Assertion failed")
        }
    }

    override fun setMultiParameters(i: IntArray, f: FloatArray, d: DoubleArray) {
        println("setMultiParameters ${i.joinToString(separator = ",")} - ${d.joinToString(separator = ",")} - ${f.joinToString(separator = ",")}")
    }
}