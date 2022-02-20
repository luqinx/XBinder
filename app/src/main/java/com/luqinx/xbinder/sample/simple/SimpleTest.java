package com.luqinx.xbinder.sample.simple;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinchao
 * @since 2022/1/13
 */
public class SimpleTest {

    private static final Map<String, Class<?>> stringMap = new HashMap<>();

    private static final SparseArray<Class<?>> sparseArray = new SparseArray<>();

    public static void main(String[] args) {
        Object i = '5';
        System.out.println(i instanceof Integer);
    }

    public void test(List list) {

    }

}

class Box {
    List<? extends  Fruit> fruits = new ArrayList<>();

}

class Fruit {

}

class Apple extends Fruit {

}

class Banana extends Fruit {

}