package com.luqinx.xbinder

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.text.TextUtils
import com.luqinx.xbinder.ChannelProvider.coreChannel
import com.luqinx.xbinder.serialize.ClassAdapter
import com.luqinx.xbinder.serialize.ParcelAdapter

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
abstract class XBinderProvider : ContentProvider() {
    
    companion object {
        internal const val TAG = "IpcProvider"

        internal const val EXTRA_KEY_BINDER = "extra.key.service"

        private val DEFAULT_COLUMNS = arrayOf("ids")

        internal val processName: String
            get() = initProcessName()!!

        val applicationId: String
            get() = contextService.packageName()

        private var mProcessName: String? = null

        private fun initProcessName(): String? {
            if (!TextUtils.isEmpty(mProcessName)) {
                return mProcessName
            }

            try {
                val runningApps = contextService.runningAppProcesses()
                if (runningApps != null) {
                    for (processInfo in runningApps) {
                        if (processInfo.pid == Process.myPid()) {
                            mProcessName = processInfo.processName
                        }
                    }
                }
                if (TextUtils.isEmpty(mProcessName)) {
                    mProcessName = contextService.packageName()
                }
            } catch (e: Exception) {
                exceptionHandler.handle(e)
            }
            mProcessName = mProcessName?.replace(":", ".")
            return mProcessName
        }
    }



    override fun onCreate(): Boolean {
        logger.d(message = "provider $javaClass onCreate")
        applicationContext = context
        val options = onInitOptions(context)
        if (options != null) {
            exceptionHandler = options.exceptionHandler
            debuggable = options.debuggable
            invokeThreshold = options.invokeThreshold
            classloader = options.classLoader
            binderDeathHandler = options.binderDeathHandler ?: BinderDeathHandler.IGNORE
            logger = options.logger
        }
        ClassAdapter.avoidReflection(avoidReflectionClasses())
        XBinder.xbinderReady = true
        return false
    }

    abstract fun onInitOptions(context: Context?): XBinderInitOptions?

    protected fun registerTypeAdapter(type: Class<*>, adapter: ParcelAdapter<*>) {
        XBinder.registerTypeAdapter(type, adapter)
    }

    protected fun addServiceFinder(serviceFinder: IServiceFinder) {
        XBinder.addServiceFinder(serviceFinder)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        logger.d(message = "provider query")
        return BinderCursor(coreChannel)
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
            binderExtra.classLoader = XBinderProvider::class.java.classLoader
            binderExtra.putParcelable(EXTRA_KEY_BINDER, stub)
        }
    }

    abstract fun avoidReflectionClasses(): List<Class<*>>?
}