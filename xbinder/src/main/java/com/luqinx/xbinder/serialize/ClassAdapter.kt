package com.luqinx.xbinder.serialize

import android.os.IBinder
import android.os.Parcel
import com.luqinx.xbinder.XBinderException
import com.luqinx.xbinder.exceptionHandler
import com.luqinx.xbinder.misc.Refined
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object ClassAdapter: ParcelAdapter<Class<*>> {

    internal val avoidReflectionClassMap = hashMapOf<String, Class<*>>()
    
    internal val buildInAvoidReflectionClassMap = mutableMapOf<String, Class<*>?>(
        "byte" to Byte::class.java,
        "short" to Short::class.java,
        "char" to Char::class.java,
        "int" to Int::class.java,
        "long" to Long::class.java,
        "float" to Float::class.java,
        "double" to Double::class.java,
        "boolean" to Boolean::class.java,
        "void" to Void::class.java,

        "[I" to IntArray::class.java,
        "[F" to FloatArray::class.java,
        "[D" to DoubleArray::class.java,
        "[Z" to BooleanArray::class.java,
        "[J" to LongArray::class.java,
        "[B" to ByteArray::class.java,
        "[C" to CharArray::class.java,

        "java.lang.String" to String::class.java,
        "java.lang.Object" to Any::class.java,
        "java.lang.reflect.Type" to Type::class.java,
        "java.lang.Class" to Class::class.java,
        "[Ljava.lang.String;" to Array<String>::class.java,
        "[Ljava.lang.Object;" to Array<Any>::class.java,
        "[Ljava.lang.reflect.Type;" to Array<Type>::class.java,
        "android.os.IBinder" to IBinder::class.java,
        "" to null
    )

    internal val avoidReflectionHistoryCache = mutableMapOf<String, Class<*>>()

    internal val avoidReflectionAdapterTypeCache = ConcurrentHashMap<String, Class<*>>()

    fun avoidReflection(avoidReflectionClasses: List<Class<*>>?) {
        avoidReflectionClasses?.map { it.name to it }?.toMap(
            avoidReflectionClassMap)
    }

    override fun readInstance(
        component: Type,
        parcel: Parcel
    ): Class<*>? {
        if (parcel.readInt() == 0) {
            return null
        }

        return parcel.readString()?.toClass()

    }

    override fun writeInstance(value: Class<*>?, component: Type, parcel: Parcel) {
        if (value == null) parcel.writeInt(0) else {
            parcel.writeInt(1)
            parcel.writeString(value.name ?: "")
        }
    }

    override fun handles(type: Type): Boolean {
        return type == Class::class.java
    }
}


fun String?.toClass(): Class<*>? {
    return when (this) {
        null -> null
        else -> {
            ClassAdapter.buildInAvoidReflectionClassMap[this] ?:
            ClassAdapter.avoidReflectionHistoryCache[this] ?:
            ClassAdapter.avoidReflectionAdapterTypeCache[this] ?:
            ClassAdapter.avoidReflectionClassMap[this] ?: try {
                Refined.start()
                Class.forName(this).also {
                    ClassAdapter.avoidReflectionHistoryCache[this] = it
                }
            } catch (e: ClassNotFoundException) {
                exceptionHandler.handle(e)
                throw XBinderException(
                    XBinderException.EXCEPTION_CLASS_NOT_FOUND,
                    e.message,
                    e
                )
            } finally {
                Refined.finish("deserialize $this by reflection! ")
            }
        }
    }
}