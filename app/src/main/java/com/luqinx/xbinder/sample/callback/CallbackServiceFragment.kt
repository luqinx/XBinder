package com.luqinx.xbinder.sample.callback

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
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

    private val callbackService = App.getRemoteService(LightBinderCallbackService::class.java)

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position) { // 普通接口回调
            0 -> {
                callbackService.invokeCallback(callbackInstance)
            }
            1 -> { // 匿名内部类回调
                App.getRemoteService(LightBinderCallbackService::class.java).invokeCallback(object : Callback{
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
            6 -> {
                App.getRemoteService(SimpleCallbackService::class.java).invokeCallback(object: Callback{
                    override fun onCallback() {
                        println("SimpleCallbackService.onCallback invoked from remote")
                    }
                })
            }
            7 -> {
                App.getRemoteService(SimpleCallbackService::class.java).invokeSimpleCallback(object: SimpleCallback {
                    override fun onCallback() {
                        println("SimpleCallbackService.onCallback invoked from remote")
                    }

                })
            }
            8 -> {
                callbackService.invokeCallback(object: LifecycleCallback{
                    override fun onCallback() {
                        println("LifecycleCallback.onCallback invoked from remote")
                    }

                    override fun getLifecycleOwner(): LifecycleOwner {
                        return viewLifecycleOwner
                    }
                })
            }
        }
    }

    override fun getObjects(): Any {
        return arrayOf(
            "0.普通接口回调",
            "1.匿名内部类回调",
            "2.普通接口数组",
            "3.接口为null",
            "4.接口在Object数组中(暂不支持)",
            "5.remote进程GC(远程代理回收,会触发本地LightBinder回收)",
            "6.普通接口服务(没有继承ILightBinder或IBinderCallback)",
            "7.普通接口回调(没有继承ILightBinder或IBinderCallback)",
            "8.IBinderCallback接口，支持Lifecycle",
        )
    }
}