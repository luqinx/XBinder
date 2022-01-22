package com.luqinx.xbinder

import java.lang.reflect.Method

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object BinderBehaviors {


    fun <T: IBinderService> invokeMethod(clazz: Class<*>, method: Method, args: Array<Any?>?, options: BinderOptions<T>, delegateId: Int): Any? {
        val rpcArgument = ChannelMethodArgument()
        rpcArgument.fromProcess = thisProcess
        rpcArgument.clazz = clazz
        rpcArgument.method = method.name
        rpcArgument.genericArgTypes = method.genericParameterTypes
        rpcArgument.args = args
        rpcArgument.delegateId = delegateId
        rpcArgument.returnType = method.returnType
//        rpcArgument.consArgs = options.constructorArgs
//        rpcArgument.consTypes = options.constructorTypes
        return BinderInvoker.invokeMethod(options.processName, rpcArgument)
    }

}