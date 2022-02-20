package com.luqinx.xbinder.serialize

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object AdapterManager {

    private val adapterMap = hashMapOf<Type, ParcelAdapter<*>>()

    private val whiteList = hashSetOf<Class<*>>(
        Byte::class.java, ByteArray::class.java,
        Char::class.java, CharArray::class.java,
        Short::class.java, ShortArray::class.java,
        Int::class.java, IntArray::class.java,
        Long::class.java, LongArray::class.java,
        Float::class.java, FloatArray::class.java,
        Double::class.java, DoubleArray::class.java,
        Boolean::class.java,BooleanArray::class.java,
        String::class.java, Array<String>::class.java,
    )
    
    init {
        register(Class::class.java, ClassAdapter)
        register(Type::class.java, GenericAdapter)
        register(ParameterizedType::class.java, GenericAdapter)
        register(GenericArrayType::class.java, ParameterizedTypeAdapter)
        register(Byte::class.java, PrimitiveAdapter) // for multi-dimensional byte array
        register(Char::class.java, PrimitiveAdapter) // for multi-dimensional char array
        register(Short::class.java, PrimitiveAdapter) // for multi-dimensional short array
        register(Int::class.java, PrimitiveAdapter)    // for multi-dimensional int array
        register(Long::class.java, PrimitiveAdapter)  // for multi-dimensional long array
        register(Float::class.java, PrimitiveAdapter) // for multi-dimensional float array
        register(Double::class.java, PrimitiveAdapter) // for multi-dimensional double array
        register(Boolean::class.java, PrimitiveAdapter) // for multi-dimensional boolean array
        register(Any::class.java, ObjectAdapter)
    }

    fun <T: ParcelAdapter<*>> register(type: Type, adapter: T) {
        adapterMap[type] = adapter
        if (type is Class<*>) {
            ClassAdapter.avoidReflectionAdapterTypeCache[type.name] = type
        }
    }

    fun unregister(type: Type) {
        adapterMap.remove(type)
    }

    fun getAdapter(type: Type): ParcelAdapter<*>? {
        if (isInWhitList(type)) {
            return null
        }
        if  (type.rawType() == List::class.java && type.actualTypeArguments()!![0] in whiteList) {
            return null
        }
        return adapterMap[type] ?: adapterMap[type.rawType()]
    }

    fun isInWhitList(type: Type): Boolean {
        return type.rawType() in whiteList
    }

}