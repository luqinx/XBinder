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
    private val isCallback: Boolean = false
): IProxyBehaviors {

    override fun `_$newConstructor_`(consTypes: Array<Class<*>>?, consArgs: Array<*>?): Boolean {
        val argument = ChannelMethodArgument()
        argument.clazz = serviceClass
        argument.method = CORE_METHOD_NEW_CONSTRUCTOR
        argument.args = arrayOf(consTypes, consArgs)
        argument.genericArgTypes = arrayOf(Array::class.java, Array::class.java)
        argument.returnType = Boolean::class.java
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

}