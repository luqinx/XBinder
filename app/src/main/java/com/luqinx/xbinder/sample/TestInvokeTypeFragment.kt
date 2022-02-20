package com.luqinx.xbinder.sample

import android.view.View
import android.widget.AdapterView
import chao.app.ami.base.AmiSimpleListFragment

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
class TestInvokeTypeFragment: AmiSimpleListFragment() {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun getObjects(): Any {
        return arrayOf(
            "local only",
            "local first & local exist",
            "local first & local not exist",
            "remote only",
        )
    }

    private fun testLocalOnly() {

    }
}