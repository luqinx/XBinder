package com.luqinx.xbinder.sample;

import android.app.Application;

import com.luqinx.xbinder.IBinderService;
import com.luqinx.xbinder.XBinder;

/**
 * @author qinchao
 * @since 2022/1/5
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public static <T extends IBinderService> T getRemoteBinder(Class<T> binderClass) {
        return XBinder.getBinder(binderClass, ":remote");
    }

    public static <T extends IBinderService> T getAppBinder(Class<T> binderClass) {
//        return XBinder.getBinder(binderClass, BuildConfig.APPLICATION_ID); // 同下面等价
//        return XBinder.getBinder(binderClass, "");   // 同下面等价
        return XBinder.getBinder(binderClass); // 默认主进程
    }
}
