package com.luqinx.xbinder.keepalive

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Process
import com.luqinx.xbinder.XBinderProvider
import com.luqinx.xbinder.contextService
import com.luqinx.xbinder.interactiveProcessMap
import com.luqinx.xbinder.logger
/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
enum class KeepAliveStrategy {
    KEEP_ALIVE, IGNORE, CUSTOM;

    private var strategyHandler: AliveStrategyHandler? = null

    fun setCustomAliveStrategyHandler(handler: AliveStrategyHandler?) {
        this.strategyHandler = handler
    }

    fun onBinderDeath(process: String) {
        when(this) {
            KEEP_ALIVE -> doKeepAlive(process)
            IGNORE -> doIgnore()
            CUSTOM -> strategyHandler?.onBinderDeath(process)
        }
    }

    private fun doKeepAlive(process: String) {
        var action: String? = null
        when {
            process.contains(":") -> {
                action = process
            }
            interactiveProcessMap.keys.contains(process) -> {
                action = process
            }
            else -> {
                interactiveProcessMap.forEach { (applicationId, list) ->
                    list.forEach { processName ->
                        if (process == processName) {
                            action = "$applicationId.$process"
                        }
                    }
                }
            }
        }
        if (action == null) {
            logger.w(message = "keep alive service action(${action}) not found!!!, current process is ${XBinderProvider.processName}(${Process.myPid()})")
            return
        }
        val intent = Intent(action)
        contextService.startService(intent)
        contextService.bindService(intent, object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                service?.linkToDeath( {
                    contextService.startService(intent)
                    contextService.bindService(intent, this, 0)
                },0)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                logger.d(message = "onServiceDisconnected: $name")
            }
        }, 0)
    }

    private fun doIgnore() {
        // do nothing
    }

    interface AliveStrategyHandler {
        fun onBinderDeath(process: String)
    }
}