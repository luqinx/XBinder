package com.luqinx.xbinder.sample.callback

import android.view.View
import android.widget.AdapterView
import chao.app.ami.base.AmiSimpleFragment
import chao.app.ami.base.AmiSimpleListFragment
import com.luqinx.xbinder.XBinder
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
        when(position) {
            0 -> {
                callbackService.invokeCallback(callbackInstance)
            }
            1 -> {
                App.getRemoteService(CallbackService::class.java).invokeCallback(object : Callback{
                    override fun onCallback() {
                        println("onCallback invoked from remote")
                    }
                })
            }
        }
    }

    override fun getObjects(): Any {
        return arrayOf(
            "普通接口回调",
            "匿名内部类回调",
            "普通接口数组",
        )
    }
}