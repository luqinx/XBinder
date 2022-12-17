package com.luqinx.xbinder

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.net.Uri
import kotlin.contracts.contract

/**
 * @author  qinchao
 *
 * @since 2022/12/17
 */
interface ContextService {
    class DefaultContextService: ContextService

    fun queryProvider(uri: Uri): Cursor? {
        ensureContext()
        return applicationContext!!.contentResolver.query(uri, null, null, null, null)
    }

    fun queryProvider(uri: String): Cursor? = queryProvider(Uri.parse(uri))

    fun packageName(): String {
        ensureContext()
        return applicationContext!!.packageName
    }

    fun runningAppProcesses(): List<ActivityManager.RunningAppProcessInfo>? {
        ensureContext()
        applicationContext!!.let {
            val am = it.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return am.runningAppProcesses;
        }
    }

    fun startService(intent: Intent) {
        ensureContext()
        applicationContext!!.startService(intent)
    }

    fun bindService(intent: Intent, serviceConnection: ServiceConnection, flags: Int) {
        ensureContext()
        applicationContext!!.bindService(intent, serviceConnection, flags)
    }

    companion object {
        private fun ensureContext() {
            if (applicationContext == null) throw NullPointerException("applicationContext is null.")
        }
    }
}