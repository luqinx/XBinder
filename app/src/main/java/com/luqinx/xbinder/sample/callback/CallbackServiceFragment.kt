package com.luqinx.xbinder.sample.callback

import android.view.View
import android.widget.AdapterView
import chao.app.ami.base.AmiSimpleListFragment
import com.luqinx.xbinder.sample.App

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
class CallbackServiceFragment: AmiSimpleListFragment() {

    private val callbackInstance = object: Callback {
        override fun onCallback() {
            println("callbackInstance callbacked")
        }
    }

    private val callbackService = App.getRemoteService(CallbackService::class.java)

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position) { // 普通接口回调
            0 -> {
                callbackService.invokeCallback(callbackInstance)
            }
            1 -> { // 匿名内部类回调
                App.getRemoteService(CallbackService::class.java).invokeCallback(object : Callback{
                    override fun onCallback() {
                        println("onCallback invoked from remote")
                    }
                })
            }
            2 -> {
                callbackService.invokeCallbacks(arrayOf(
                    object: Callback {
                        override fun onCallback() {
                            println("callback0 invoked from remote")
                        }
                    },
                    object: Callback {
                        override fun onCallback() {
                            println("callback1 invoked from remote")
                        }
                    },
                    object: Callback {
                        override fun onCallback() {
                            println("callback2 invoked from remote")
                        }
                    },
                ))
            }
            3 -> { // 接口为null
                callbackService.invokeCallback(null)
            }
            4 -> {
                // not support yet
                callbackService.invokeObjects(arrayOf(
                    1,
                    "hello",
                    object: Callback {
                        override fun onCallback() {
                            println("callback in objects invoked from remote")
                        }
                    }
                ))
            }
            5 -> {
                callbackService.remoteGc()
            }
        }
    }

    override fun getObjects(): Any {
        return arrayOf(
            "普通接口回调",
            "匿名内部类回调",
            "普通接口数组",
            "接口为null",
            "接口在Object数组中",
            "remote进程GC"
        )
    }
}