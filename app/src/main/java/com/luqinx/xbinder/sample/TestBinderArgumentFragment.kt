package com.luqinx.xbinder.sample

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.View
import android.widget.AdapterView
import chao.app.ami.Ami
import chao.app.ami.base.AmiSimpleListFragment
import com.luqinx.xbinder.sample.simple.PrimitiveArrayTypeService
import com.luqinx.xbinder.sample.simple.PrimitiveBoxArrayTypeService
import com.luqinx.xbinder.sample.simple.PrimitiveTypeService
import com.luqinx.xbinder.sample.simple.SimpleTest
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType

/**
 * @author  qinchao
 *
 * @since 2022/1/9
 */
class TestBinderArgumentFragment: AmiSimpleListFragment() {

    private val data = arrayOf("0. 接口类型", "1. 简单泛型", "2. Parcelable,Serialize类型", "3. 基本类型", "4. 基本类型数组", "5. 基本类型包装类数组", "6. 基本类型列表", "7. 多维数组")

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> testInterfaceTypes()
            1 -> testSimpleGenericTypes()
            2 -> testSerializeTypes()
            3 -> testPrimitiveTypes()
            4 -> testPrimitiveArrays()
            5 -> testPrimitiveBoxArrays()
            6 -> test()
        }
    }

    fun onTestMethodParameter(sList: List<String>, sListArray: Array<List<String>>, fragment: TestBinderArgumentFragment) {

    }

    private fun test() {

//        println("call class: " + Reflection.getCallerClass())
        val start = SystemClock.elapsedRealtimeNanos()
        try {
            1/ 0
        } catch (e: Throwable) {
            println("1/0 spent ${SystemClock.elapsedRealtimeNanos() - start}ns")
            e.printStackTrace()
            println("1/0 spent ${SystemClock.elapsedRealtimeNanos() - start}ns after stack trace")
        }

        val typeArrayClass = Class.forName("[Ljava.lang.reflect.Type;")
        println(typeArrayClass.javaClass)
        println(typeArrayClass.componentType)
        println(typeArrayClass.isArray)

        TestBinderArgumentFragment::class.java.methods.filter {
            it.name == "onTestMethodParameter"
        }.forEach {
            val arrayType = it.genericParameterTypes[1] as GenericArrayType
            val parameterType = arrayType.genericComponentType as ParameterizedType
            Ami.log("arrayType: $arrayType")
            Ami.log("ownerType: ${parameterType.ownerType}")
            Ami.log("rawType:   ${parameterType.rawType}")
            Ami.log("actualTypes: ${parameterType.actualTypeArguments.joinToString(",")}")

            Ami.log("${it.name}(${it.genericParameterTypes.joinToString(",")})")
            Ami.log("${it.name}(${it.parameterTypes.joinToString(",")})")

            Ami.log("${it.parameterTypes[2]}")
            Ami.log("${it.genericParameterTypes[2]}")
        }

        Ami.log()

        TestBinderArgumentFragment::class.java.methods.filter {
            it.name == "onItemClick"
        }.forEach {
            Ami.log("${it.name}(${it.genericParameterTypes.joinToString(",")})")
            Ami.log("${it.name}(${it.parameterTypes.joinToString(",")})")
        }

        Ami.log()

        SimpleTest::class.java.methods.filter {
            it.name == "test"
        }.forEach {
            Ami.log("${it.name}(${it.genericParameterTypes.joinToString(",")})")
        }
    }

    @SuppressLint("Assert")
    private fun testPrimitiveBoxArrays() {
        val charArray = Array(5) { "hello"[it] }
        val booleanArray = arrayOf(true, false, true)
        val shortArray = arrayOf(1.toShort(), 2.toShort(), 3.toShort())
        val doubleArray = arrayOf(4.0, 5.0, 6.0)
        val floatArray = arrayOf(7.0f, 8.0f ,9.0f)
        val intArray = arrayOf(10, 11, 12)
        val longArray = arrayOf(13L, 14L, 15L)
        val stringArray = arrayOf("16", "17", null)
        val byteArray = arrayOf(18.toByte(), 19, 20)

        val service = App.getRemoteService(PrimitiveBoxArrayTypeService::class.java)
        service.setCharArray(charArray)
        service.setBooleanArray(booleanArray)
        service.setByteArray(byteArray)
        service.setDoubleArray(doubleArray)
        service.setFloatArray(floatArray)
        service.setShortArray(shortArray)
        service.setIntArray(intArray)
        service.setLongArray(longArray)
        service.setStringArray(stringArray)
        service.setNull(null)

        assert(charArray.contentEquals(service.getCharArray()!!))
        assert(booleanArray.contentEquals(service.getBooleanArray()!!))
        assert(byteArray.contentEquals(service.getByteArray()!!))
        assert(doubleArray.contentEquals(service.getDoubleArray()!!))
        assert(floatArray.contentEquals(service.getFloatArray()!!))
        assert(shortArray.contentEquals(service.getShortArray()!!))
        assert(intArray.contentEquals(service.getIntArray()!!))
        assert(longArray.contentEquals(service.getLongArray()!!))
        assert(stringArray.contentEquals(service.getStringArray()!!))
        assert(service.getNull() == null)

    }

    @SuppressLint("Assert")
    private fun testPrimitiveArrays() {
        val service = App.getRemoteService(PrimitiveArrayTypeService::class.java)
        service.setCharArray(charArrayOf('h', 'e', 'l', 'l', 'o'))
        service.setBooleanArray(booleanArrayOf(true, false, true))
        service.setByteArray(byteArrayOf(1, 2, 3))
        service.setDoubleArray(doubleArrayOf(4.0, 5.0, 6.0))
        service.setFloatArray(floatArrayOf(7.0f, 8.0f, 9.0f))
        service.setIntArray(intArrayOf(10, 11, 12))
        service.setLongArray(longArrayOf(13L, 14L, 15L))
        service.setNull(null)

        service.setMultiParameters(intArrayOf(16, 17, 18), floatArrayOf(19.0f, 20.0f, 21.0f), doubleArrayOf(22.0, 23.0, 24.0))

        assert(charArrayOf('h', 'e', 'l', 'l', 'o').contentEquals(service.getCharArray()))
        assert(booleanArrayOf(true, false, true).contentEquals(service.getBooleanArray()))
        assert(byteArrayOf(1, 2, 3).contentEquals(service.getByteArray()))
        assert(doubleArrayOf(4.0, 5.0, 6.0).contentEquals(service.getDoubleArray()))
        assert(floatArrayOf(7.0f, 8.0f, 9.0f).contentEquals(service.getFloatArray()))
        assert(intArrayOf(10, 11, 12).contentEquals(service.getIntArray()))
        assert(longArrayOf(13L, 14L, 15L).contentEquals(service.getLongArray()))
        assert(service.getNull() == null)
    }

    private fun testInterfaceTypes() {
        TODO("Not yet implemented")
        // XxxService
        // XxxService: IBinderService
        // XxxService: IBinderCallback
    }

    private fun testSerializeTypes() {
        TODO("Not yet implemented")
        // XxxArg: Serializable
        // XxxArg: Parcelable
        // array of XxxArg: Serializable
        // array of XxxArg: Parcelable
    }

    private fun testSimpleGenericTypes() {
        // 入参出参为基本类型的泛型
        // 入参出参为基本类型的泛型数组
        // 入参出参为
    }

    override fun getObjects(): Any {
        return data
    }

    @SuppressLint("Assert")
    private fun testPrimitiveTypes() {
        val service = App.getRemoteService(PrimitiveTypeService::class.java)
        service.run()
        assert(service.getBoolean())
        assert(service.getByte() == 'b'.toByte())
        assert(service.getChar() == 'i')
        assert(service.getShort() == 1.toShort())
        assert(service.getInt() == 2)
        assert(service.getLong() == 3L)
        assert(service.getFloat() == 4.0f)
        assert(service.getDouble() == 5.0)
        assert(service.getString() == "6 String")
        assert(service.getNull() == null)
        service.setBoolean(true)
        service.setByte("10".toByte())
        service.setChar(11.toChar())
        service.setShort(12)
        service.setInt(13)
        service.setLong(14L)
        service.setFloat(15.0f)
        service.setDouble(16.0)
        service.setString("this is setString 17")
        service.setNull(null)
        service.setMultiParameters(86, "17130044315", 3.14f)
    }
}