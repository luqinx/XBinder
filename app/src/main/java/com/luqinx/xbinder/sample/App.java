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

    public static <T extends IBinderService> T getRemoteService(Class<T> binderClass) {
        return XBinder.getService(binderClass, ":remote");
    }

    public static <T extends IBinderService> T getAppService(Class<T> binderClass) {
//        return XBinder.getService(binderClass, BuildConfig.APPLICATION_ID); // 同下面等价
//        return XBinder.getService(binderClass, "");   // 同下面等价
        return XBinder.getService(binderClass); // 默认主进程
    }
}
