package com.luqinx.xbinder

import com.luqinx.xbinder.annotation.AsyncCall
import com.luqinx.xbinder.annotation.InvokeType
import com.luqinx.xbinder.annotation.OnewayCall
import com.luqinx.xbinder.misc.Refined
import java.lang.reflect.Method

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object BinderBehaviors {


    fun <T: IBinderService> invokeMethod(clazz: Class<*>, method: Method, args: Array<Any?>?, options: NewServiceOptions<T>, delegateId: Int): Any? {
        val rpcArgument = ChannelArgument()
        rpcArgument.fromProcess = thisProcess
        rpcArgument.clazz = clazz.name
        rpcArgument.method = method.name
        rpcArgument.genericArgTypes = method.genericParameterTypes
        rpcArgument.args = args
        rpcArgument.delegateId = delegateId
        rpcArgument.returnType = method.returnType.name

        if (options is NewCallbackOptions) {
            rpcArgument.instanceId = options.instanceId
        }

        if (XBinder.hasGradlePlugin()) {
            // todo
        } else {
            Refined.start()
            rpcArgument.asyncCall = method.getAnnotation(AsyncCall::class.java) != null
            rpcArgument.onewayCall = if (rpcArgument.asyncCall) rpcArgument.asyncCall else
                method.getAnnotation(OnewayCall::class.java) != null
            Refined.finish("async annotation ")
        }
        return BinderInvoker.invokeMethod(
            options.processName,
            rpcArgument,
            options.invokeType == InvokeType.REMOTE_FIRST
        )
    }

}