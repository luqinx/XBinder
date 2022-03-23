package com.luqinx.xbinder

import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class ProxyBehaviors(
    private val remoteProcessName: String,
    private val serviceClass: Class<*>,
    private val delegateId: Int,
    private var _instanceId: String?
): IProxyBehaviors {

    override fun `_$newConstructor_`(consTypes: Array<*>?, consArgs: Array<*>?): String? {
        val argument = ChannelArgument()
        argument.clazz = serviceClass.name
        argument.method = CORE_METHOD_NEW_CONSTRUCTOR
        argument.args = consArgs
        argument.genericArgTypes = consTypes as Array<out Type>?
        argument.returnType = "boolean"
        argument.delegateId = delegateId
        return BinderInvoker.invokeMethod(remoteProcessName, argument, false) as String?
    }

    override fun `_$bindCallbackProxy_`(instanceId: String): Boolean {
        val argument = ChannelArgument()
        argument.clazz = serviceClass.name
        argument.method = CORE_METHOD_BIND_CALLBACK_PROXY
        argument.returnType = "boolean"
        argument.delegateId = delegateId
        return BinderInvoker.invokeMethod(remoteProcessName, argument, false) as Boolean? ?: false
    }

    override fun getRemoteProcessName(): String {
        return remoteProcessName
    }

    override fun isRemoteServiceExist(): Boolean {
        return super.isRemoteServiceExist()
    }

    override fun isBinderAlive(): Boolean {
//        return if (isCallback) {
//            RemoteServiceStore.isBinderDeath(remoteProcessName)
//        } else {
//            false
//        }
        return false
    }

    override fun getInstanceId(): String? {
        return _instanceId
    }

    override fun setInstanceId(instanceId: String?) {
        _instanceId = instanceId
    }
}