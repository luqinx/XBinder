package com.luqinx.xbinder.sample.simple

import android.os.IBinder
import com.luqinx.xbinder.ILightBinder

/**
 * @author  qinchao
 *
 * @since 2022/2/19
 */
interface BinderTypeService: ILightBinder {
    fun getOnewayBinder(): IBinder

    fun newOnewayBinder(): IBinder
}