package com.luqinx.xbinder.sample.callback

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
import chao.app.ami.base.AmiSimpleListFragment
import com.luqinx.xbinder.ILightBinder
import com.luqinx.xbinder.sample.App
import com.luqinx.xbinder.sample.TimeAssert

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
class LightBinderServiceFragment: AmiSimpleListFragment() {

    private val callbackInstance = object: Callback {
        override fun onCallback() {
            println("callbackInstance callbacked")
        }
    }

    private val lightBinderService = App.getRemoteService(LightBinderCallbackService::class.java)

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position) { // 普通接口回调
            0 -> {
                lightBinderService.invokeCallback(callbackInstance)
            }
            1 -> { // 匿名内部类回调
                val assert = TimeAssert.startCountDown(1)
                App.getRemoteService(LightBinderCallbackService::class.java).invokeCallback(object : Callback{
                    override fun onCallback() {
                        assert.countDown()
                        println("onCallback invoked from remote")
                    }
                })
            }
            2 -> {
                val assert = TimeAssert.startCountDown(3)
                lightBinderService.invokeCallbacks(arrayOf(
                    object: Callback {
                        override fun onCallback() {
                            assert.countDown()
                            println("callback0 invoked from remote")
                        }
                    },
                    object: Callback {
                        override fun onCallback() {
                            assert.countDown()
                            println("callback1 invoked from remote")
                        }
                    },
                    object: Callback {
                        override fun onCallback() {
                            assert.countDown()
                            println("callback2 invoked from remote")
                        }
                    },
                ))
            }
            3 -> { // 接口为null
                lightBinderService.invokeCallback(null)
            }
            4 -> {
                // not support yet
                lightBinderService.invokeObjects(arrayOf(
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
                lightBinderService.remoteGc()
            }
            6 -> {
                val assert = TimeAssert.startCountDown(1)
                App.getRemoteService(SimpleCallbackService::class.java).invokeCallback(object: Callback{
                    override fun onCallback() {
                        assert.countDown()
                        println("SimpleCallbackService.onCallback invoked from remote")
                    }
                })
            }
            7 -> {
                val assert = TimeAssert.startCountDown(1)
                App.getRemoteService(SimpleCallbackService::class.java).invokeSimpleCallback(object: SimpleCallback {
                    override fun onCallback() {
                        assert.countDown()
                        println("SimpleCallbackService.onCallback invoked from remote")
                    }
                })
            }
            8 -> {
                val assert = TimeAssert.startCountDown(1)
                lightBinderService.invokeCallback(object: LifecycleCallback{
                    override fun onCallback() {
                        assert.countDown()
                        println("LifecycleCallback.onCallback invoked from remote")
                    }

                    override fun getLifecycleOwner(): LifecycleOwner {
                        return viewLifecycleOwner
                    }
                })
            }
            9 -> {
                val lightBinder = lightBinderService.getLightBinder()
                TimeAssert.assert(lightBinder!!.getInstanceId()!!.isNotEmpty())
                lightBinder.onCallback()
            }
            10 -> {
                val simpleLightBinder = lightBinderService.getSimpleLightBinder()
                TimeAssert.assert((simpleLightBinder as ILightBinder).getInstanceId()!!.isNotEmpty())
                simpleLightBinder.onCallback()
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
            "6.普通接口服务(没有继承ILightBinder)",
            "7.普通接口回调(没有继承ILightBinder)",
            "8.支持Lifecycle",
            "9.返回类型是ILightBinder",
            "10.返回类型是普通接口"
        )
    }
}