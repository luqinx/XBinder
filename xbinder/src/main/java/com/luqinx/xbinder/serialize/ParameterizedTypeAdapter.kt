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
        component: Type,
        parcel: Parcel
    ): ParameterizedType? {
        val rawType = ClassAdapter.read(component, parcel) as Type
        val argumentCount = parcel.readInt()
        val actualTypeArguments = Array<Type>::class.java.newArrayInstance<Type>(argumentCount)!!
        for (i in 0 until argumentCount) {
            actualTypeArguments[i] = GenericAdapter.read(Type::class.java, parcel) as Type
        }
        return ParameterizedTypeImpl(null, rawType, actualTypeArguments as Array<Type>)
    }

    override fun writeInstance(value: ParameterizedType?, component: Type,parcel: Parcel) {
        value?.apply {
            parcel.writeInt(1)
            ClassAdapter.writeInstance(value.rawType as Class<*>, value, parcel)

            val argumentCount = actualTypeArguments.size
            parcel.writeInt(argumentCount)
            for (i in 0 until argumentCount) {
                GenericAdapter.write(actualTypeArguments[i], actualTypeArguments[i], parcel)
            }
        } ?: run {
            parcel.writeInt(-1)
        }

    }

    override fun handles(type: Type): Boolean {
        return type == ParameterizedType::class.java
    }

}