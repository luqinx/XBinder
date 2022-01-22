package com.luqinx.xbinder

import android.app.ActivityManager
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.text.TextUtils

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
open class BinderContentProvider: ContentProvider() {
    
    companion object {
        internal const val TAG = "IpcProvider"

        internal const val EXTRA_KEY_BINDER = "extra.key.service"

        private val DEFAULT_COLUMNS = arrayOf("ids")

        internal val processName: String
            get() = initProcessName()!!

        val applicationId: String
            get() = context.packageName

        private var mProcessName: String? = null

        private fun initProcessName(): String? {
            if (!TextUtils.isEmpty(mProcessName)) {
                return mProcessName
            }
            if (Build.VERSION.SDK_INT >= 28) {
                mProcessName = Application.getProcessName()
            } else {
                try {
                    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    val runningApps = am.runningAppProcesses
                    if (runningApps != null) {
                        for (processInfo in runningApps) {
                            if (processInfo.pid == Process.myPid()) {
                                mProcessName = processInfo.processName
                            }
                        }
                    }
                    if (TextUtils.isEmpty(mProcessName)) {
                        mProcessName = context.packageName
                    }
                } catch (e: Exception) {
                    exceptionHandler.handle(e)
                }
            }
            mProcessName = mProcessName?.replace(":", ".")
            return mProcessName
        }
    }

    private val coreService = object : BinderChannelService.Stub() {
        override fun invokeMethod(rpcArgument: ChannelMethodArgument): ChannelMethodResult {
            logger.d(message = "invokeMethod by ${rpcArgument.delegateId}: ${rpcArgument.returnType} ${rpcArgument.method}(${rpcArgument.args?.let {
                return@let (it as Array<*>).contentDeepToString()
            } ?: ""})")
            val result = ChannelMethodResult()
            result.succeed = true
            val clazzImpl: IBinderService?
            if (rpcArgument.method == CORE_METHOD_NEW_CONSTRUCTOR) {
                rpcArgument.run {
                    val start = System.currentTimeMillis()
                    clazzImpl = ServiceProvider.doFind(fromProcess, delegateId, clazz, args?.get(0) as Array<Class<*>>?, args?.get(1) as Array<*>? )
                    result.value = clazzImpl != null
                    result.invokeConsumer = System.currentTimeMillis() - start
                    return result
                }
            } else {
                rpcArgument.run {
                    clazzImpl = ServiceProvider.getServiceImpl(fromProcess, delegateId)
                }
            }

            if (clazzImpl == null) {
                result.succeed = false
                result.errMessage = "the impl of ${rpcArgument.clazz} not found."
                return result
            }
            try {
                if (rpcArgument.argTypes == null || rpcArgument.argTypes!!.isEmpty()) {
                    val method = clazzImpl.javaClass.getDeclaredMethod(rpcArgument.method)
                    method.isAccessible = true
                    val start = System.currentTimeMillis()
                    result.value = method.invoke(clazzImpl)
                    result.invokeConsumer = System.currentTimeMillis() - start
                } else {
                    val method = clazzImpl.javaClass.getDeclaredMethod(rpcArgument.method, *rpcArgument.argTypes!!)
                    method.isAccessible = true
                    val start = System.currentTimeMillis()
                    result.value = method.invoke(clazzImpl, *rpcArgument.args!!)
                    result.invokeConsumer = System.currentTimeMillis() - start
                }
            } catch (e: Throwable) {
                result.succeed = false
                result.errMessage = e.message
                exceptionHandler.handle(e)
            }
            return result
        }

        override fun unRegisterCallbackMethod(fromProcess: String, methodId: String) {
//            ServiceStore.unregisterMethodCallback(fromProcess, methodId)
        }
    }
    
    override fun onCreate(): Boolean {
        println("provider onCreate")
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        println("provider query")
        return BinderCursor(coreService)
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
    


    class BinderCursor(binder: IBinder): MatrixCursor(DEFAULT_COLUMNS) {
        
        private val binderExtra: Bundle = Bundle()

        private val stub = ParcelableBinder(binder)

        override fun getExtras(): Bundle {
            return binderExtra
        }

        init {
            binderExtra.classLoader = BinderContentProvider::class.java.classLoader
            binderExtra.putParcelable(EXTRA_KEY_BINDER, stub)
        }
    }
}