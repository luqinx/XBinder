package com.luqinx.xbinder.serialize

import android.os.Parcel
import com.luqinx.xbinder.XBinderException
import com.luqinx.xbinder.exceptionHandler
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object ClassAdapter: ParcelAdapter<Class<*>> {

    override fun readInstance(
        component: Type,
        parcel: Parcel
    ): Class<*>? {
        return when (val clazzName = parcel.readString()) {
            "byte" -> Byte::class.java
            "short" -> Short::class.java
            "char" -> Char::class.java
            "int" -> Int::class.java
            "long" -> Long::class.java
            "float" -> Float::class.java
            "double" -> Double::class.java
            "boolean" -> Boolean::class.java
            "void" -> Void::class.java
            "java.lang.String" -> String::class.java
            "" -> null
            null -> null
            else -> {
                try {
                    Class.forName(clazzName)
                } catch (e: ClassNotFoundException) {
                    exceptionHandler.handle(e)
                    throw XBinderException(XBinderException.EXCEPTION_CLASS_NOT_FOUND, e.message, e)
                }
            }
        }
    }

    override fun writeInstance(value: Class<*>?, component: Type, parcel: Parcel) {
        parcel.writeString(value?.name ?: "")
    }

    override fun handles(type: Type): Boolean {
        return type == Class::class.java
    }
}