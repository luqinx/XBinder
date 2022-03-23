package com.luqinx.xbinder.serialize

import android.os.Parcel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.luqinx.xbinder.*
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
object LightBinderAdapter: ParcelAdapter<ILightBinder> {

    override fun readInstance(parcel: Parcel, component: Type): ILightBinder? {
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
        value: ILightBinder?,
        component: Type
    ) {
        value?.apply {
            if (value is IProxyFlag) { // for serialize a proxy like BinderProxy
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

                val invokeProcess = BinderInvoker.invokeProcess()
                ServiceProvider.registerServiceInstance(
                    invokeProcess,
                    instanceId,
                    this
                )
                if (this is IBinderCallback) {
                    getLifecycleOwner()?.lifecycle?.addObserver(object : LifecycleEventObserver{
                        override fun onStateChanged(
                            source: LifecycleOwner,
                            event: Lifecycle.Event
                        ) {
                            if (event == Lifecycle.Event.ON_DESTROY) {
                                ServiceProvider.unregisterServiceInstance(invokeProcess, instanceId)
                            }
                        }

                    })
                }
            }
        } ?: run {
            parcel.writeInt(-1)
        }
    }

}