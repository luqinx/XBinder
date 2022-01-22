package com.luqinx.xbinder

import com.luqinx.xbinder.keepalive.KeepAliveStrategy
import java.util.ArrayList

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */

internal const val CORE_METHOD_NEW_CONSTRUCTOR = "_\$newConstructor_"

internal val context by lazy { XBinder.context }

internal val thisProcess by lazy { BinderContentProvider.processName }

internal val options = XBinder.initOptions

internal var keepAliveStrategy: KeepAliveStrategy = KeepAliveStrategy.IGNORE

internal var logger: ILogger = options.logger ?: ILogger.SimpleLogger

internal val interactiveProcessMap = hashMapOf<String, List<String>>()

internal var serviceFinders: ArrayList<IServiceFinder> = arrayListOf()

internal var exceptionHandler = options.exceptionHandler ?: XBinderExceptionHandler.SimpleHandler
