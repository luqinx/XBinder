package com.luqinx.xbinder.sample

import android.view.View
import android.widget.AdapterView
import chao.app.ami.UI
import chao.app.ami.base.AmiSimpleListFragment
import com.luqinx.xbinder.sample.async.TestAsyncOnewayFragment
import com.luqinx.xbinder.sample.callback.LightBinderServiceFragment
import com.luqinx.xbinder.sample.invoketype.TestInvokeTypeFragment
import com.luqinx.xbinder.sample.simple.TestBinderArgumentFragment

/**
 * @author  qinchao
 *
 * @since 2022/3/22
 */
class FunctionListFragment: AmiSimpleListFragment() {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position) {
            0 -> UI.show(context, TestBinderArgumentFragment::class.java)
            1 -> UI.show(context, TestAsyncOnewayFragment::class.java)
            2 -> UI.show(context, LightBinderServiceFragment::class.java)
            3 -> UI.show(context, TestInvokeTypeFragment::class.java)
        }
    }

    override fun getObjects(): Any {
        return arrayOf(
            "数据类型",
            "oneway | async",
            "轻量binder(接口)",
            "降级策略",
        )
    }
}