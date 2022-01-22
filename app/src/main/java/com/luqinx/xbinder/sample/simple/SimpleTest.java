package com.luqinx.xbinder.sample.simple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinchao
 * @since 2022/1/13
 */
public class SimpleTest {

    public static void main(String[] args) {

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