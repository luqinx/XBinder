package com.luqinx.xbinder.sample.simple

/**
 * @author  qinchao
 *
 * @since 2022/1/5
 */
class PrimitiveTypeServiceImpl: PrimitiveTypeService {
    override fun run() {
        println("invoke method run()")
    }

    override fun getChar(): Char {
        return 'i'
    }

    override fun getByte(): Byte {
        return 'b'.toByte()
    }

    override fun getShort(): Short {
        return 1.toShort()
    }

    override fun getInt(): Int {
        return 2
    }

    override fun getLong(): Long {
        return 3L
    }

    override fun getFloat(): Float {
        return 4.0f
    }

    override fun getDouble(): Double {
        return 5.0
    }

    override fun getBoolean(): Boolean {
        return true
    }

    override fun getString(): String {
        return "6 String"
    }

    override fun getNull(): String? {
        println("invoke method null")
        return null
    }

    override fun setChar(c: Char) {
        println("invoke setChar $c")
    }

    override fun setByte(b: Byte) {
        println("invoke setByte $b")
    }

    override fun setShort(s: Short) {
        println("invoke setShort $s")
    }

    override fun setInt(i: Int) {
        println("invoke setInt $i")
    }

    override fun setLong(l: Long) {
        println("invoke setLong $l")
    }

    override fun setFloat(f: Float) {
        println("invoke setFloat $f")
    }

    override fun setDouble(d: Double) {
        println("invoke setDouble $d")
    }

    override fun setBoolean(b: Boolean) {
        println("invoke setBoolean $b")
    }

    override fun setString(s: String) {
        println("invoke setString $s")
    }

    override fun setNull(s: String?) {
        println("invoke setNull $s")
    }

    override fun setMultiParameters(i: Int, s: String, f: Float) {
        println("setMultiParameters $i-$s-$f")
    }


}