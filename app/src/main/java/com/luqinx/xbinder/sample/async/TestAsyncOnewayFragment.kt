package com.luqinx.xbinder.sample.async

import android.os.SystemClock
import android.view.View
import android.widget.AdapterView
import chao.app.ami.base.AmiSimpleListFragment
import com.luqinx.xbinder.sample.App
import com.luqinx.xbinder.sample.aidl.Oneway
import com.luqinx.xbinder.sample.simple.BinderTypeService

/**
 * @author  qinchao
 *
 * @since 2022/2/19
 */
class TestAsyncOnewayFragment : AmiSimpleListFragment() {

    companion object {
        private const val testJava = false
        private val stringMap = hashMapOf<String, Class<*>>()

        private val sparseArray = hashMapOf<Int, Class<*>>()

    }

    private val caller = App.getRemoteService(AsyncCallService::class.java)
//        val caller = XBinder.getService(AsyncCallJavaService::class.java)

    private val onewayCallerBinder = App.getRemoteService(BinderTypeService::class.java)
    private val aidlCaller = Oneway.Stub.asInterface(onewayCallerBinder.getOnewayBinder())


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val start = SystemClock.elapsedRealtimeNanos()
        when (position) {
            0 -> {
                caller.onewayCall()
                println("call oneway spent: ${SystemClock.elapsedRealtimeNanos() - start}")
            }
            1 -> {
                repeat(100) {
                    caller.asyncCall()
                }
                println("call async spent: ${SystemClock.elapsedRealtimeNanos() - start}")
            }
            2 -> {
                caller.normalCall()
                println("call normal spent: ${SystemClock.elapsedRealtimeNanos() - start}")
            }
            3 -> {
                aidlCaller.onewayCall()
                println("aidl call oneway spent: ${SystemClock.elapsedRealtimeNanos() - start}")
            }
            4 -> {
                aidlCaller.normalCall()
                println("aidl call normal spent: ${SystemClock.elapsedRealtimeNanos() - start}")
            }
            5 -> {

//                repeat(50) {
//                    Executors.newFixedThreadPool(40).execute {
//                        caller.onewayCall()
//                        caller.normalCall()
////                        aidlCaller.normalCall()
////                        aidlCaller.onewayCall()
//                    }
//                }
//                UI.show(context, TestBinderArgumentFragment::class.java)

                repeat(10000) {
                    val binder = Oneway.Stub.asInterface(onewayCallerBinder.newOnewayBinder())
                    binders.add(binder)
                }
            }
        }
    }

    val binders = arrayListOf<Oneway>()

    override fun getObjects(): Any {
        return arrayOf(
            "oneway call",
            "async call",
            "normal call",
            "aidl oneway call",
            "aidl normal call",
            "test"
        )
    }
}