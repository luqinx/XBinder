package com.luqinx.xbinder.sample.async;

import com.luqinx.xbinder.ILightBinder;
import com.luqinx.xbinder.annotation.AsyncCall;
import com.luqinx.xbinder.annotation.OnewayCall;

/**
 * @author qinchao
 * @since 2022/2/19
 */
public interface AsyncCallJavaService extends ILightBinder {
    @AsyncCall
    void asyncCall();

    @OnewayCall
    void onewayCall();

    void normalCall();
}
