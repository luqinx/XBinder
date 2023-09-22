package com.luqinx.xbinder

import java.util.*

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class ObjectBehaviors(
    private val serviceClass: Class<*>,
    private val processName: String,
    private val instanceId: String?
) {

    val uuid = UUID.randomUUID().toString()

    override fun toString(): String {
        return "proxy of ${serviceClass.name} @${uuid}"
    }

    fun finalize() {
        if (instanceId == null) return
        val argument = ChannelArgument()
        argument.clazz = serviceClass.name
        argument.method = CORE_METHOD_UNREGISTER_INSTANCE
        argument.returnType = "boolean"
        argument.delegateId = null
        argument.instanceId = instanceId
        argument.onewayCall = true
        argument.asyncCall = true
        BinderInvoker.invokeMethod(processName, argument, false)
    }


    override fun equals(other: Any?): Boolean {
        return Objects.equals(other, this)
    }

}