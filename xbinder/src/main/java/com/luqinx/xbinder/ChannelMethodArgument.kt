package com.luqinx.xbinder

import android.os.Parcel
import android.os.Parcelable
import com.luqinx.xbinder.serialize.ClassAdapter
import com.luqinx.xbinder.serialize.ObjectAdapter
import com.luqinx.xbinder.serialize.GenericAdapter
import com.luqinx.xbinder.serialize.rawType
import java.lang.IllegalArgumentException
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class ChannelMethodArgument() : Parcelable {

    internal lateinit var clazz: Class<*>

    internal lateinit var method: String

    internal lateinit var returnType: Type

    internal var args: Array<Any?>? = null

    internal var genericArgTypes: Array<Type>? = null

    internal lateinit var fromProcess: String

    internal var delegateId: Int = -1

    internal val argTypes: Array<Class<*>>?
        get() {
            val size = genericArgTypes?.size ?: 0
            val types = arrayOfNulls<Class<*>>(size)
            for (i in 0 until size) {
                val argType = genericArgTypes?.get(i)!!
                types[i] = when (argType) {
                    is Class<*> -> argType
                    is Array<*> -> argType.javaClass
                    is ParameterizedType -> argType.rawType as Class<*>
                    is GenericArrayType -> argType.rawType() as Class<*>
                    else -> { throw IllegalArgumentException("unknown arg type: $argType") }
                }
            }
            return types as Array<Class<*>>?
        }

    constructor(parcel: Parcel) : this() {
        fromProcess = parcel.readString()!!
        clazz = ClassAdapter.readInstance(Class::class.java, parcel)!! as Class<*>
        method = parcel.readString()!!
        returnType = GenericAdapter.read(Class::class.java, parcel)!! as Class<*>
        delegateId = parcel.readInt()

        genericArgTypes = GenericAdapter.read(Array::class.java, parcel) as Array<Type>?
        args = ObjectAdapter.read(genericArgTypes?.javaClass!! , parcel) as Array<Any?>?
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(XBinder.currentProcessName())
        ClassAdapter.writeInstance(clazz, clazz.javaClass, parcel)
        parcel.writeString(method)
        GenericAdapter.write(returnType, returnType.javaClass,  parcel)
        parcel.writeInt(delegateId)
        try {
            GenericAdapter.write(genericArgTypes, genericArgTypes?.javaClass ?: Array<Type>::class.java ,parcel)
            ObjectAdapter.write(args, args?.javaClass ?: Array<Any>::class.java, parcel)
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChannelMethodArgument> {
        override fun createFromParcel(parcel: Parcel): ChannelMethodArgument {
            return ChannelMethodArgument(parcel)
        }

        override fun newArray(size: Int): Array<ChannelMethodArgument?> {
            return arrayOfNulls(size)
        }
    }
}