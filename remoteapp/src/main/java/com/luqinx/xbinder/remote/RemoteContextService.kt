package com.luqinx.xbinder.remote

import android.app.ActivityManager
import android.app.ActivityManagerNative
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.util.Log
import com.luqinx.xbinder.ContextService
import rikka.hidden.compat.ActivityManagerApis

/**
 * @author  qinchao
 *
 * @since 2022/12/17
 */
class RemoteContextService(private val applicationContext: Context): ContextService {
    override fun packageName(): String {
        return applicationContext.packageName
    }

    override fun queryProvider(uri: Uri): Cursor? {
        return ActivityManagerApis.getContentProviderExternal(uri.toString(), 0, null, uri.toString()).also {
                Log.d("xbinder","query content provider: $it")
            }?.query("com.luqinx.devkits.codecrafts", uri, null, null, null, null, null)
    }

    override fun runningAppProcesses(): List<ActivityManager.RunningAppProcessInfo>? {
        return ActivityManagerNative.getDefault().runningAppProcesses
    }

    override fun startService(intent: Intent) {

    }

    override fun bindService(intent: Intent, serviceConnection: ServiceConnection, flags: Int) {

    }
}