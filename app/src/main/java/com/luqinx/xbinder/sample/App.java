package com.luqinx.xbinder.sample;

import android.app.Application;

import com.luqinx.xbinder.ILightBinder;
import com.luqinx.xbinder.XBinder;

/**
 * @author qinchao
 * @since 2022/1/5
 */
public class App extends Application {

    private static Application sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public static <T> T getRemoteService(Class<T> binderClass) {
        return XBinder.getService(binderClass, ":remote");
    }

    public static <T extends ILightBinder> T getAppService(Class<T> binderClass) {
//        return XBinder.getService(binderClass, BuildConfig.APPLICATION_ID); // 同下面等价
//        return XBinder.getService(binderClass, "");   // 同下面等价
        return XBinder.getService(binderClass); // 默认主进程
    }

    public static Application getApp() {
        return sApp;
    }
}
