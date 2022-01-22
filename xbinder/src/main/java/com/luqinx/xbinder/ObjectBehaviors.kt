package com.luqinx.xbinder

import java.util.*

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class ObjectBehaviors(private val serviceClass: Class<*>) {

    private val createTime: Long = System.currentTimeMillis()

    override fun toString(): String {
        return "proxy of ${serviceClass.name} @${hashCode()}"
    }

    override fun hashCode(): Int {
        return createTime.hashCode() xor super.hashCode()
    }

    fun finalize() {

    }

    override fun equals(other: Any?): Boolean {
        return Objects.equals(other, this)
    }

}