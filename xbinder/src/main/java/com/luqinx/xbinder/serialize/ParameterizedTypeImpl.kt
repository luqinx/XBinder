package com.luqinx.xbinder.serialize

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ParameterizedTypeImpl(
    private var ownerType: Type?,
    private var rawType: Type,
    private var actualTypeArguments: Array<Type>
) : ParameterizedType {
    override fun getRawType(): Type {
        return rawType
    }

    override fun getOwnerType(): Type? {
        return ownerType
    }

    override fun getActualTypeArguments(): Array<Type> {
        return actualTypeArguments
    }
}
