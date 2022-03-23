package com.luqinx.xbinder.serialize

import android.os.Parcel
import com.luqinx.xbinder.ILightBinder
import com.luqinx.xbinder.classloader
import com.luqinx.xbinder.exceptionHandler
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
    fun read(parcel: Parcel, component: Type): Any? {
        try {
            GenericAdapter.readInstance(parcel, component)?.apply {
                val adapter = AdapterManager.getAdapter(this, component)

                adapter?.also {
                    return when {
                        this.isClassArray() -> {
                            it.readClassArray(parcel, this as Class<*>)
                        }
                        this.isGenericArray() -> {
                            it.readGenericArray(parcel, this as GenericArrayType)
                        }
                        else -> {
                            it.readInstance(parcel, this)
                        }
                    }
                } ?: run {
                    return parcel.readValue(classloader)
                }
            }
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        }
        return null
    }

    fun readInstance(parcel: Parcel, component: Type): T?

    fun readArray(
        parcel: Parcel,
        types: Array<out Type?>
    ): Array<Any?>? {
        val arrayType: Type?
        val size = parcel.readInt()
        return if (size >= 0) {
            arrayType = GenericAdapter.readInstance(parcel, types.javaClass.componentType!!)
            val array = arrayType!!.newArrayInstance<Any>(size)!!
            for (i in 0 until size) {
                array[i] = read(parcel, types[i]!!)
            }
            array
        } else null
    }

    fun readClassArray(
        parcel: Parcel,
        type: Class<*>
    ): Array<Any?>? {
        val size = parcel.readInt()
        val array: Array<Any?>?
        if (size >= 0) {
            val componentType = type.componentType!!
            array = componentType.newArrayInstance(size)!!
            for (i in 0 until size) {
                array[i] = read(parcel, componentType)
            }
        }  else {
            array = null
        }
        return array
    }

    fun readGenericArray(
        parcel: Parcel,
        type: GenericArrayType
    ): Array<Any?>? {
        val size = parcel.readInt()
        val array: Array<Any?>?
        if (size >= 0) {
            array = type.newArrayInstance(size)
            for (i in 0 until size) {
                array?.set(i, read(parcel, type))
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
    fun write(parcel: Parcel, value: Any?, basicComponent: Type) {
        try {
            val component =
                if (value is ILightBinder) ILightBinder::class.java else value?.javaClass
                    ?: basicComponent
            GenericAdapter.writeInstance(parcel, component, component)
            val adapter = AdapterManager.getAdapter(component, basicComponent) as ParcelAdapter<Any>?
            adapter?.apply {
                if (basicComponent.isClassArray()) {
                    this.writeClassArray(parcel, value as Array<*>?, basicComponent as Class<*>)
                } else if (basicComponent.isGenericArray()) {
                    this.writeGenericArray(parcel, value as Array<*>?, basicComponent as GenericArrayType)
                } else {
                        this.writeInstance(parcel, value, basicComponent)
                }
            } ?: run {
                parcel.writeValue(value)
            }
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        }
    }

    fun writeInstance(parcel: Parcel, value: T?, component: Type)

    fun writeArray(parcel: Parcel, values: Array<*>?, types: Array<out Type?>) {
        when (values) {
            null -> parcel.writeInt(-1)
            else -> {
                parcel.writeInt(types.size)
                GenericAdapter.writeInstance(
                    parcel,
                    values.javaClass.componentType,
                    values.javaClass.componentType!!
                )
                for (i in types.indices) {
                    write(parcel, values[i], types[i] ?: Type::class.java)
                }
            }
        }
    }

    fun writeClassArray(parcel: Parcel, array: Array<*>?, arrayType: Class<*>) {
        val size = array?.size ?: -1
        if (size >= 0) {
            parcel.writeInt(size)
            for (i in 0 until size) {
//                val type = array?.get(i)?.javaClass?.componentType() ?: arrayType.componentType
                write(parcel, array?.get(i), arrayType.componentType as Type)
            }
        } else {
            parcel.writeInt(-1)
        }
    }

    fun writeGenericArray(
        parcel: Parcel,
        array: Array<out Any?>?,
        component: GenericArrayType
    ) {
        val size = array?.size ?: -1
        if (size >= 0) {
            parcel.writeInt(size)
            for (i in 0 until size) {
                val type = array?.get(i)?.javaClass?.componentType() ?: component.genericComponentType
                write(parcel, array?.get(i), type)
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

}