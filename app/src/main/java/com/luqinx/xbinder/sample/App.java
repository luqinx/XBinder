package com.luqinx.xbinder.sample;

import android.app.Application;

import com.luqinx.xbinder.ILightBinder;
import com.luqinx.xbinder.XBinder;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

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
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, "6e13fa93f531cef804521621b628c513");

        TimeAssert.Companion.start();
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
