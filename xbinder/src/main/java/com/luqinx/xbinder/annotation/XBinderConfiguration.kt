package com.luqinx.xbinder.annotation

/**
 * @author  qinchao
 *
 * @since 2022/2/19
 */
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class XBinderConfiguration(
    val requirePermission: String = "",
)
