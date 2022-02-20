package com.luqinx.xbinder

import android.os.Parcel
import android.os.Parcelable
import com.luqinx.xbinder.misc.Refined
import com.luqinx.xbinder.serialize.ClassAdapter
import com.luqinx.xbinder.serialize.ObjectAdapter
import com.luqinx.xbinder.serialize.GenericAdapter
import com.luqinx.xbinder.serialize.rawType
import java.lang.IllegalArgumentException
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.sql.Ref

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class ChannelMethodArgument() : Parcelable {

    @JvmField
    internal var onewayCall = false

    @JvmField
    internal var asyncCall = false

    internal lateinit var clazz: String

    internal lateinit var method: String

    internal lateinit var returnType: String

    internal var args: Array<*>? = null

    internal var genericArgTypes: Array<*>? = null

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
        Refined.start()
        fromProcess = parcel.readString()!!
        clazz = parcel.readString()!!
        method = parcel.readString()!!
        returnType = parcel.readString()!!
        asyncCall = parcel.readInt() != 0
        onewayCall = parcel.readInt() != 0
        delegateId = parcel.readInt()

        val paramsCount = parcel.readInt()
        if (paramsCount > 0) {
            genericArgTypes = GenericAdapter.read(Array::class.java, parcel) as Array<Type>?
            args = ObjectAdapter.read(genericArgTypes?.javaClass!!, parcel) as Array<Any?>?
        }
        Refined.finish("$method readParcel")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        Refined.start()
        parcel.writeString(XBinder.currentProcessName())
        parcel.writeString(clazz)
        parcel.writeString(method)
        parcel.writeString(returnType)
        parcel.writeInt(if (asyncCall) 1 else 0)
        parcel.writeInt(if (onewayCall) 1 else 0)
        parcel.writeInt(delegateId)
        try {
            val paramsCount = genericArgTypes?.size ?: 0
            parcel.writeInt(paramsCount)
            if (paramsCount > 0) {
                GenericAdapter.write(
                    genericArgTypes,
                    genericArgTypes?.javaClass ?: Array<Type>::class.java,
                    parcel
                )
                ObjectAdapter.write(args, args?.javaClass ?: Array<Any>::class.java, parcel)
            }
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        }
        Refined.finish("$method writeToParcel")
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