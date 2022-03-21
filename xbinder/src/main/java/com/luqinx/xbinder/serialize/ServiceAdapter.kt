package com.luqinx.xbinder.serialize

import android.os.Parcel
import com.luqinx.xbinder.*
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
object ServiceAdapter: ParcelAdapter<IBinderService> {

    override fun readInstance(parcel: Parcel, component: Type): IBinderService? {
        if (parcel.readInt() < 0) return null
        val instanceId = parcel.readString()!!
        val process = parcel.readString()!!
        val serviceClazz = GenericAdapter.readInstance(parcel, component) as Class<IBinderService>

        return ServiceProvider.getServiceInstance(instanceId) // don't create proxy if in local
            ?: ServiceProxyFactory.newCallbackProxy(
                NewCallbackOptions(
                    serviceClazz,
                    process,
                    instanceId,
                )
            )
    }

    override fun writeInstance(
        parcel: Parcel,
        value: IBinderService?,
        component: Type
    ) {
        value?.apply {
            parcel.writeInt(1)
            val instanceId = "${javaClass}-${hashCode()}"
            parcel.writeString(instanceId)
            parcel.writeString(XBinder.currentProcessName())
            GenericAdapter.writeInstance(parcel, component, component)
            ServiceProvider.registerServiceInstance(instanceId, this)
        } ?: run {
            parcel.writeInt(-1)
        }
    }

    override fun handles(type: Type): Boolean {
        TODO("Not yet implemented")
    }
}