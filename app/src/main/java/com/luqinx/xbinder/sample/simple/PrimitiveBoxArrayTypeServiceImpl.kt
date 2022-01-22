package com.luqinx.xbinder.sample.simple

import com.luqinx.xbinder.sample.BuildConfig

/**
 * @author  qinchao
 *
 * @since 2022/1/8
 */
class PrimitiveBoxArrayTypeServiceImpl: PrimitiveBoxArrayTypeService {

    private var charArray: Array<Char>? = null
    private var byteArray: Array<Byte>? = null
    private var shortArray: Array<Short>? = null
    private var intArray: Array<Int>? = null
    private var longArray: Array<Long>? = null
    private var floatArray: Array<Float>? = null
    private var doubleArray: Array<Double>? = null
    private var booleanArray: Array<Boolean>? = null
    private var stringArray: Array<String?>? = null

    override fun getCharArray(): Array<Char>? {
        return charArray
    }

    override fun getByteArray(): Array<Byte>? {
        return byteArray
    }

    override fun getShortArray(): Array<Short>? {
        return shortArray
    }

    override fun getIntArray(): Array<Int>? {
        return intArray
    }

    override fun getLongArray(): Array<Long>? {
        return longArray
    }

    override fun getFloatArray(): Array<Float>? {
        return floatArray
    }

    override fun getDoubleArray(): Array<Double>? {
        return doubleArray
    }

    override fun getBooleanArray(): Array<Boolean>? {
        return booleanArray
    }

    override fun getStringArray(): Array<String?>? {
        return stringArray
    }

    override fun getNull(): Array<String?>? {
        return null
    }

    override fun setCharArray(c: Array<Char>) {
        charArray = c
    }

    override fun setByteArray(b: Array<Byte>) {
        byteArray = b
    }

    override fun setShortArray(s: Array<Short>) {
        shortArray = s
    }

    override fun setIntArray(i: Array<Int>) {
        intArray = i
    }

    override fun setLongArray(l: Array<Long>) {
        longArray = l
    }

    override fun setFloatArray(f: Array<Float>) {
        floatArray = f
    }

    override fun setDoubleArray(d: Array<Double>) {
        doubleArray = d
    }

    override fun setBooleanArray(b: Array<Boolean>) {
        booleanArray = b
    }

    override fun setStringArray(s: Array<String?>) {
        stringArray = s
    }

    override fun setNull(s: Array<Int?>?) {
        if (BuildConfig.DEBUG && s != null) {
            error("Assertion failed")
        }
    }

    override fun setMultiParameters(i: Array<Int>, f: Array<Float>, d: Array<Double>) {
        println("setMultiParameters ${i.joinToString(separator = ",")} - ${d.joinToString(separator = ",")} - ${f.joinToString(separator = ",")}")
    }
}