package com.luqinx.xbinder.keepalive

import android.app.Service
import android.content.Intent
import android.os.IBinder
/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
open class KeepAliveService: Service() {

    private val keepAlive = object: KeepAlive.Stub() { }

    override fun onBind(intent: Intent?): IBinder {
        return keepAlive
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY // service will be startup by other process if necessary
    }
}

class AliasService1: KeepAliveService()
class AliasService2: KeepAliveService()
class AliasService3: KeepAliveService()
class AliasService4: KeepAliveService()
class AliasService5: KeepAliveService()
class AliasService6: KeepAliveService()


