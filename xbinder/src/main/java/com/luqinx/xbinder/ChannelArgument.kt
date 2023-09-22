package com.luqinx.xbinder

import android.os.Parcel
import com.luqinx.xbinder.misc.Refined
import com.luqinx.xbinder.serialize.ObjectAdapter
import com.luqinx.xbinder.serialize.GenericAdapter
import com.luqinx.xbinder.serialize.rawType
import java.lang.IllegalArgumentException
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.UUID

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class ChannelArgument() {

    companion object {
        private val sThreadLocal = ThreadLocal<String>()

        var toProcess: String
            get() {
                return sThreadLocal.get()!!
            }
            set(value) {
                sThreadLocal.set(value)
            }

        fun removeFromThreadLocal() {
            sThreadLocal.remove()
        }

    }


    @JvmField
    internal var onewayCall = false

    @JvmField
    internal var asyncCall = false

    internal lateinit var clazz: String

    internal lateinit var method: String

    internal lateinit var returnType: String

    internal var args: Array<*>? = null

    internal var genericArgTypes: Array<out Type>? = null

    internal lateinit var fromProcess: String

    internal var delegateId: String? = null

    internal var instanceId: String? = null

    internal val uuid = UUID.randomUUID().toString()

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
        delegateId = parcel.readString()
        instanceId = parcel.readString()

        val paramsCount = parcel.readInt()
        if (paramsCount > 0) {
            genericArgTypes = GenericAdapter.readArray(parcel, Array(paramsCount) { Type::class.java }) as Array<Type>?
            args = ObjectAdapter.readArray(parcel, genericArgTypes!!)
        }
        Refined.finish("$method readParcel")
    }

    fun writeToParcel(parcel: Parcel, flags: Int) {
        Refined.start()
        parcel.writeString(XBinder.currentProcessName())
        parcel.writeString(clazz)
        parcel.writeString(method)
        parcel.writeString(returnType)
        parcel.writeInt(if (asyncCall) 1 else 0)
        parcel.writeInt(if (onewayCall) 1 else 0)
        parcel.writeString(delegateId)
        parcel.writeString(instanceId)
        try {
            val paramsCount = genericArgTypes?.size ?: 0
            parcel.writeInt(paramsCount)
            if (paramsCount > 0) {
                GenericAdapter.writeArray(
                    parcel,
                    genericArgTypes!!,
                    arrayOfNulls(paramsCount)
                )
                ObjectAdapter.writeArray(
                    parcel,
                    args,
                    genericArgTypes as Array<Type>,
                )
            }
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        }
        Refined.finish("$method writeToParcel")
    }

    fun describeContents(): Int {
        return 0
    }
}