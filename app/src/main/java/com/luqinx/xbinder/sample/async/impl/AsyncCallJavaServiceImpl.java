package com.luqinx.xbinder.sample.async.impl;

import com.luqinx.xbinder.sample.async.AsyncCallJavaService;

/**
 * @author qinchao
 * @since 2022/2/19
 */
public class AsyncCallJavaServiceImpl implements AsyncCallJavaService {
    @Override
    public void asyncCall() {
        System.out.println("async call start");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("async call end!!");
    }

    @Override
    public void onewayCall() {
        System.out.println("oneway call start");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("oneway call end!!");

    }

    @Override
    public void normalCall() {

    }
}
