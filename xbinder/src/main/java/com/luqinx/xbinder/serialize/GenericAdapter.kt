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
        component: Type,
        parcel: Parcel
    ): Type? {
        var type: Type? = null
        when (parcel.readInt()) {
            CLASS_TYPE -> {
                type = ClassAdapter.readInstance(component, parcel)
            }
            PARAMETERIZED_TYPE -> {
                type = ParameterizedTypeAdapter.readInstance(component, parcel)
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

    override fun writeInstance(value: Type?, component: Type, parcel: Parcel) {
        parcel.writeInt(component.int())
        when (component) {
            is Class<*> -> {
                ClassAdapter.writeInstance(value as Class<*>?, component, parcel)
            }
            is ParameterizedType -> {
                ParameterizedTypeAdapter.writeInstance(value as ParameterizedType?, component, parcel)
            }
            is GenericArrayType -> {

            }
        }
    }
}