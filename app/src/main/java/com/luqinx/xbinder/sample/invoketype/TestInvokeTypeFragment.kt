package com.luqinx.xbinder.sample.invoketype

import android.view.View
import android.widget.AdapterView
import chao.app.ami.base.AmiSimpleListFragment
import com.luqinx.xbinder.XBinder
import com.luqinx.xbinder.annotation.InvokeType
import com.luqinx.xbinder.sample.callback.SimpleCallback
import com.luqinx.xbinder.sample.callback.SimpleCallbackService

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
class TestInvokeTypeFragment: AmiSimpleListFragment() {

    private fun <T> getRemoteService(service: Class<T>, @InvokeType invokeType: Int): T {
        return XBinder.getService(service, processName = "com.luqinx.xbinder.sample.remote", invokeType_ =  invokeType)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position) {
            0 -> {
                getRemoteService(
                    SimpleCallbackService::class.java,
                    InvokeType.REMOTE_ONLY
                ).invokeSimpleCallback(object : SimpleCallback {
                    override fun onCallback() {
                        println("remote only callback invoked(${Thread.currentThread().name}")
                    }
                })
            }
            1 -> {
                getRemoteService(
                    SimpleCallbackService::class.java,
                    InvokeType.REMOTE_FIRST
                ).invokeSimpleCallback(object : SimpleCallback {
                    override fun onCallback() {
                        println("remote first & exist callback invoked(${Thread.currentThread().name})")
                    }
                })
            }
        }
    }

    override fun getObjects(): Any {
        return arrayOf(
            "0. remote only", // 如果远程binder实现不存在会有错误日志
            "1. remote first & remote exist",
            "2. remote first & remote not exist",
            "3. local only",
            "4. local first & local exist",
            "5. local first & local not exist",
        )
    }

    private fun testLocalOnly() {

    }
}