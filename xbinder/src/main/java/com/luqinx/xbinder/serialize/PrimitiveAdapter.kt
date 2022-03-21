package com.luqinx.xbinder.serialize

import android.os.Parcel
import java.lang.reflect.Type

/**
 *
 * for multi-dimensional primitive array
 *
 * @author  qinchao
 *
 * @since 2022/1/9
 */
object PrimitiveAdapter: ParcelAdapter<Any> {

    private val primitives = mapOf<Type, Int>(
        Byte::class.java to Primitives.byte,
        Char::class.java to Primitives.char,
        Short::class.java to Primitives.short,
        Int::class.java to Primitives.int,
        Long::class.java to Primitives.long,
        Float::class.java to Primitives.float,
        Double::class.java to Primitives.double,
        Boolean::class.java to Primitives.boolean,
    )

    override fun readInstance(
        parcel: Parcel,
        component: Type,
    ): Any {
        checkPrimitiveType(component)
        return when (component) {
            Byte::class.java -> {
                (parcel.readInt() and 0xff).toByte()
            }
            Char::class.java -> {
                (parcel.readInt() and  0xff).toChar()
            }
            Short::class.java -> {
                (parcel.readInt() and 0xffff).toShort()
            }
            Int::class.java -> {
                parcel.readInt()
            }
            Long::class.java -> {
                parcel.readLong()
            }
            Float::class.java -> {
                parcel.readFloat()
            }
            Double::class.java -> {
                parcel.readDouble()
            }
            Boolean::class.java -> {
                parcel.readInt() == 1
            }
            else -> {
                throw IllegalStateException("unreachable code.")
            }
        }
    }

    override fun writeInstance(parcel: Parcel, value: Any?, component: Type) {
        checkPrimitiveType(component)
//        parcel.writeInt(primitives.getValue(component))
        if (value == null) {
            parcel.writeInt(0)
            return
        }
        when (component) {
            Byte::class.java -> parcel.writeInt((value as Byte).toInt())
            Char::class.java -> parcel.writeInt((value as Char).toInt()) // & 0xff
            Short::class.java -> parcel.writeInt((value as Short).toInt()) // & 0xffff
            Int::class.java -> parcel.writeInt(value as Int)
            Long::class.java -> parcel.writeLong(value as Long)
            Float::class.java -> parcel.writeFloat(value as Float)
            Double::class.java -> parcel.writeDouble(value as Double)
            Boolean::class.java -> {
                val v = if (value as Boolean) 1 else 0
                parcel.writeInt(v)
            }
        }
    }

    override fun handles(type: Type): Boolean {
        return primitives[type] != null
    }

    internal object Primitives {
        internal const val byte = 0
        internal const val char = 1
        internal const val short = 2
        internal const val int = 3
        internal const val long = 4
        internal const val float = 5
        internal const val double = 6
        internal const val boolean = 7
    }

    private fun checkPrimitiveType(type: Type) {
        if (type in primitives) {
            return
        }
        throw IllegalArgumentException("$type is not a primitive type")
    }
}