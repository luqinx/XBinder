package com.luqinx.xbinder.serialize

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/11
 */
fun getComponentType(type: Type): Type? {
    if (isArrayType(type)) {
        return when (type) {
            is Class<*> -> type.componentType
            is GenericArrayType -> type.genericComponentType
            else -> null
        }
    }
    return null
}

fun isArrayType(type: Type): Boolean {
    return when(type) {
        is Class<*> -> type.isArray
        is GenericArrayType -> true
        else -> false
    }
}

fun isGenericArrayType(type: Type): Boolean {
    return isArrayType(type) && type is GenericArrayType
}

fun isClassArrayType(type: Type): Boolean {
    return isArrayType(type) && type is Class<*>
}

fun getRawType(type: Type): Type {
    return when (type) {
        is Class<*> -> {
            if (type.isArray) {
                getRawType(type.componentType as Class<*>)
            } else {
                type
            }
        }
        is ParameterizedType -> {
            type.rawType
        }
        is GenericArrayType -> {
            getRawType(type.genericComponentType)
        }
        else -> throw IllegalArgumentException("unknown type : $type")
    }
}

const val CLASS_TYPE = 0

const val PARAMETERIZED_TYPE = 2

const val GENERIC_ARRAY_TYPE = 3

fun Type.int(): Int {
    return when (this) {
        is Class<*> -> CLASS_TYPE
        is GenericArrayType -> GENERIC_ARRAY_TYPE
        is ParameterizedType -> PARAMETERIZED_TYPE
        else -> throw IllegalArgumentException("type $this is not support")
    }
}


/** for generic type */

fun Type.actualTypeArguments(): Array<Type>? {
    if (this is ParameterizedType){
        return actualTypeArguments
    }
    return null
}

fun Type.ownerType(): Type? {
    if (this is ParameterizedType) {
        return ownerType
    }
    return null
}

fun Type.rawType(): Type? {
    return getRawType(this)
}

/** for array type */

fun Type.componentType(): Type? {
    return getComponentType(this)
}

fun Type.isGenericArray(): Boolean {
    return isGenericArrayType(this)
}

fun Type.isClassArray(): Boolean {
    return isClassArrayType(this)
}

fun Type.isArray(): Boolean {
    return isArrayType(this)
}

fun <T> Type.newArrayInstance(size: Int): Array<T?>? {
    return when (this) {
        is Class<*> -> {
            java.lang.reflect.Array.newInstance(this, size) as Array<T?>?
        }
        is GenericArrayType -> {
            java.lang.reflect.Array.newInstance(this.rawType() as Class<*>) as Array<T?>?
        }
        else -> null
    }
}
