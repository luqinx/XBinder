package com.luqinx.xbinder.sample;

import android.app.Application;
import android.content.Context;

import com.luqinx.xbinder.IBinderService;
import com.luqinx.xbinder.XBinder;
import com.luqinx.xbinder.XBinderInitOptions;
import com.luqinx.xbinder.sample.simple.PrimitiveArrayTypeService;
import com.luqinx.xbinder.sample.simple.PrimitiveArrayTypeServiceImpl;
import com.luqinx.xbinder.sample.simple.PrimitiveTypeService;
import com.luqinx.xbinder.sample.simple.PrimitiveTypeServiceImpl;

/**
 * @author qinchao
 * @since 2022/1/5
 */
public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        XBinderInitOptions options = new XBinderInitOptions();
        options.setDebuggable(true);
        options.setInvokeThreshold(XBinderInitOptions.INVOKE_THRESHOLD_FORCE_ENABLE);
        XBinder.init(this, options);

        if ((BuildConfig.APPLICATION_ID + ".remote").equals(XBinder.currentProcessName())) {
            XBinder.addServiceFinder((clazz, consTypes, constArgs) -> {
                if (clazz == PrimitiveTypeService.class) {
                    return new PrimitiveTypeServiceImpl();
                } else if (clazz == PrimitiveArrayTypeService.class) {
                    return new PrimitiveArrayTypeServiceImpl();
                }
                return null;
            });
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public static <T extends IBinderService> T getRemoteBinder(Class<T> binderClass) {
        return XBinder.getBinder(binderClass, ":remote");
    }
}
