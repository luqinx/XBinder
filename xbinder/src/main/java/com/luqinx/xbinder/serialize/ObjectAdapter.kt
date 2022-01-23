package com.luqinx.xbinder.serialize

import android.os.Parcel
import com.luqinx.xbinder.classloader
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/9
 */
object ObjectAdapter: ParcelAdapter<Any> {

    override fun handles(type: Type): Boolean {
        return true
    }

    override fun writeInstance(value: Any?, component: Type, parcel: Parcel) {
        parcel.writeValue(value)
    }

    override fun readInstance(component: Type, parcel: Parcel): Any? {
        return parcel.readValue(classloader)
    }

}