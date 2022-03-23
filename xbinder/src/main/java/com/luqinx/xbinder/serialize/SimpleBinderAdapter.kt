package com.luqinx.xbinder.serialize

import android.os.Parcel
import com.luqinx.interceptor.Interceptor
import com.luqinx.xbinder.*
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
object SimpleBinderAdapter: ParcelAdapter<Any> {

    override fun readInstance(parcel: Parcel, component: Type): Any? {
        if (parcel.readInt() < 0) return null

        val instanceId = parcel.readString()!!
        val process = parcel.readString()!!
        val serviceClazz = GenericAdapter.readInstance(parcel, component) as Class<ILightBinder>

        return ServiceProvider.getServiceInstance(
            XBinder.currentProcessName(),
            instanceId,
        ) // don't create proxy if in local
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
        value: Any?,
        component: Type
    ) {
        value?.apply {
            if (this is IProxyFlag && this is ILightBinder) { // for serialize a proxy like BinderProxy
                parcel.writeInt(2)
                parcel.writeString(getInstanceId())
                parcel.writeString(getRemoteProcessName())
                GenericAdapter.writeInstance(parcel, component, component)
            } else { // for serialize a real instance like Binder
                parcel.writeInt(1)
                val instanceId = "${javaClass}-${hashCode()}"
                parcel.writeString(instanceId)
                parcel.writeString(XBinder.currentProcessName())
                GenericAdapter.writeInstance(parcel, component, component)
                val lightBinder = if (this !is ILightBinder) Interceptor.of(this)
                    .interfaces(ILightBinder::class.java).newInstance()
                else this
                ServiceProvider.registerServiceInstance(
                    BinderInvoker.invokeProcess(),
                    instanceId,
                    lightBinder as ILightBinder
                )
            }
        } ?: run {
            parcel.writeInt(-1)
        }
    }

}