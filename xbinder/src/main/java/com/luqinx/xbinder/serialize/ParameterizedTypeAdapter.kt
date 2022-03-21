package com.luqinx.xbinder.serialize

import android.os.Parcel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/9
 */
internal object ParameterizedTypeAdapter: ParcelAdapter<ParameterizedType> {

    override fun readInstance(
        parcel: Parcel,
        component: Type,
    ): ParameterizedType {
        val rawType = ClassAdapter.read(parcel, component) as Type
        val argumentCount = parcel.readInt()
        val actualTypeArguments = Array<Type>::class.java.newArrayInstance<Type>(argumentCount)!!
        for (i in 0 until argumentCount) {
            actualTypeArguments[i] = GenericAdapter.read(parcel, component) as Type
        }
        return ParameterizedTypeImpl(null, rawType, actualTypeArguments as Array<Type>)
    }

    override fun writeInstance(
        parcel: Parcel,
        value: ParameterizedType?,
        component: Type
    ) {
        value?.apply {
            parcel.writeInt(1)
            ClassAdapter.writeInstance(parcel, value.rawType as Class<*>, component)

            val argumentCount = actualTypeArguments.size
            parcel.writeInt(argumentCount)
            for (i in 0 until argumentCount) {
                GenericAdapter.write(
                    parcel,
                    actualTypeArguments[i],
                    actualTypeArguments[i],
                )
            }
        } ?: run {
            parcel.writeInt(-1)
        }

    }

    override fun handles(type: Type): Boolean {
        return type == ParameterizedType::class.java
    }

}