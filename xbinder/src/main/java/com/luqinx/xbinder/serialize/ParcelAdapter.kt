package com.luqinx.xbinder.serialize

import android.os.Parcel
import com.luqinx.xbinder.exceptionHandler
import com.luqinx.xbinder.options
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
interface ParcelAdapter<T: Any> {

//    fun newArray(type: Type, length: Int, parcel: Parcel): Array<Any?>

    /**
     * deserialize from the parcel
     *
     *  @param component the rawType
     *
     *  if the target type is a [Class], the rawType is the same with [Class]
     *  if the target type is a [Array], the rawType is the component type of [Array]
     *  if the target type is a [java.lang.reflect.ParameterizedType], the rawType is the raw
     *  if the target type is a [GenericArrayType], the rawType is the generic component type of [GenericArrayType]
     */
    fun read(component: Type, parcel: Parcel): Any? {
        try {
            GenericAdapter.readInstance(component, parcel)?.apply {
                val adapter = AdapterManager.getAdapter(this) as ParcelAdapter?

                adapter?.also {
                    return when {
                        this.isClassArray() -> {
                            it.readArray(this as Class<*>, parcel)
                        }
                        this.isGenericArray() -> {
                            it.readGenericArray(this as GenericArrayType, parcel) 
                        }
                        else -> {
                            it.readInstance(this, parcel) 
                        }
                    }
                } ?: run {
                    return parcel.readValue(options.classLoader) 
                }
            }
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        }
        return null
    }

    fun readInstance(component: Type, parcel: Parcel): T?


    fun readArray(
        type: Class<*>,
        parcel: Parcel
    ): Array<Any?>? {
        val size = parcel.readInt()
        val array: Array<Any?>?
        if (size >= 0) {
            val componentType = type.componentType!!
            array = componentType.newArrayInstance(size)!!
            for (i in 0 until size) {
                array[i] = read(componentType, parcel = parcel)
            }
        }  else {
            array = null
        }
        return array
    }

    fun readGenericArray(
        type: GenericArrayType,
        parcel: Parcel
    ): Array<Any?>? {
        val size = parcel.readInt()
        val array: Array<Any?>?
        if (size >= 0) {
            array = type.newArrayInstance(size)
            for (i in 0 until size) {
                array?.set(i, read(type.genericComponentType, parcel))
            }
        } else {
            array = null
        }
        return array
    }

    /**
     * serialize by the parcel
     *
     *  @param component the component type
     *
     *  if the target type is a [Class], the component is the same with [Class]
     *  if the target type is a [Array], the component is the component type of [Array]
     *  if the target type is a [java.lang.reflect.ParameterizedType], the rawType is the raw
     *  if the target type is a [GenericArrayType], the component is the generic component type of [GenericArrayType]
     */
    fun write(value: Any?, component: Type, parcel: Parcel) {
        try {
            GenericAdapter.writeInstance(component, component, parcel)
            val adapter = AdapterManager.getAdapter(component) as ParcelAdapter<Any>?
            adapter?.apply {
                when  {
                    component.isClassArray() -> {
                        this.writeArray(component as Class<*>, value as Array<Any?>?, parcel)
                    }
                    component.isGenericArray() -> {
                        this.writeGenericArray(component as GenericArrayType, value as Array<Any?>?, parcel)
                    }
                    else -> {
                        this.writeInstance(value, component, parcel)
                    }
                }
            } ?: run {
                parcel.writeValue(value)
            }
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        }
    }

    fun writeInstance(value: T?, component: Type, parcel: Parcel)

    fun writeArray(arrayType: Class<*>, array: Array<out Any?>?, parcel: Parcel) {
        val size = array?.size ?: -1
        if (size >= 0) {
            parcel.writeInt(size)
            for (i in 0 until size) {
                val type = array?.get(i)?.javaClass?.componentType() ?: arrayType.componentType
                write(array?.get(i), type, parcel)
            }
        } else {
            parcel.writeInt(-1)
        }
    }

    fun writeGenericArray(component: GenericArrayType, array: Array<out Any?>?, parcel: Parcel) {
        val size = array?.size ?: -1
        if (size >= 0) {
            parcel.writeInt(size)
            for (i in 0 until size) {
                val type = array?.get(i)?.javaClass?.componentType() ?: component.genericComponentType
                write(array?.get(i), type, parcel)
            }
        } else {
            parcel.writeInt(-1)
        }
    }

//    fun writeArray(genericArrayType: GenericArrayType, array: Array<Any?>?, parcel: Parcel) {
//        val size = genericArrayType.size ?: -1
//        if (size >= 0) {
//            parcel.writeInt(size)
////            writeArrayComponentType(parcel)
//            for (i in 0 until size) {
//                write(genericArrayType[i], array?.get(i), parcel)
//            }
//        } else {
//            parcel.writeInt(-1)
//        }
//    }

    fun handles(type: Type): Boolean
}