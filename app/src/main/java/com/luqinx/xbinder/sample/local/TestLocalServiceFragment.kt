package com.luqinx.xbinder.sample.local

import android.view.View
import android.widget.AdapterView
import chao.app.ami.base.AmiSimpleListFragment
import com.luqinx.xbinder.sample.App
import com.luqinx.xbinder.sample.invoketype.impl.LocalFirstServiceImpl

class TestLocalServiceFragment: AmiSimpleListFragment() {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                App.getAppLocalService(LocalFirstServiceImpl::class.java).run()
            }
        }
    }

    override fun getObjects(): Any {
        return listOf(
            "run from local"
        )
    }

}