package com.luqinx.xbinder.serialize

import android.os.Parcel
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/9
 */
object GenericAdapter: ParcelAdapter<Type> {

    private const val CLASS_TYPE = 0

    private const val PARAMETERIZED_TYPE = 1

    private const val GENERIC_ARRAY_TYPE = 2

    override fun readInstance(
        parcel: Parcel,
        component: Type,
    ): Type? {
        var type: Type? = null
        when (parcel.readInt()) {
            CLASS_TYPE -> {
                type = ClassAdapter.readInstance(parcel, component)
            }
            PARAMETERIZED_TYPE -> {
                type = ParameterizedTypeAdapter.readInstance(parcel, component)
            }
            GENERIC_ARRAY_TYPE -> {

            }
            else -> {

            }
        }
        return type
    }

    override fun handles(type: Type): Boolean {
        return type is Class<*> || type is ParameterizedType || type is GenericArrayType
    }

    override fun writeInstance(parcel: Parcel, value: Type?, component: Type) {
        parcel.writeInt(component.int())
        when (component) {
            is Class<*> -> {
                ClassAdapter.writeInstance(parcel, value as Class<*>?, component)
            }
            is ParameterizedType -> {
                ParameterizedTypeAdapter.writeInstance(
                    parcel,
                    value as ParameterizedType?,
                    component
                )
            }
            is GenericArrayType -> {

            }
        }
    }
}