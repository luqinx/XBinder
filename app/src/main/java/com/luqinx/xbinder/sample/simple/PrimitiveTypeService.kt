package com.luqinx.xbinder.sample.simple

import com.luqinx.xbinder.IBinderService

/**
 * @author  qinchao
 *
 * @since 2022/1/5
 */
interface PrimitiveTypeService: IBinderService {
    fun run()

    fun getChar(): Char

    fun getByte(): Byte

    fun getShort(): Short

    fun getInt(): Int

    fun getLong(): Long

    fun getFloat(): Float

    fun getDouble(): Double

    fun getBoolean(): Boolean

    fun getString(): String

    fun getNull(): String?

    fun setChar(c: Char)

    fun setByte(b: Byte)

    fun setShort(s: Short)

    fun setInt(i: Int)

    fun setLong(l: Long)

    fun setFloat(f: Float)

    fun setDouble(d: Double)

    fun setBoolean(b: Boolean)

    fun setString(s: String)

    fun setNull(s: String?)

    fun setMultiParameters(i: Int, s: String, f: Float)
}