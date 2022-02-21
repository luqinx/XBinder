package com.luqinx.xbinder.sample.aidl;

interface Oneway {
    oneway void onewayCall();
    void normalCall();

    oneway void registerOneway(Oneway onewayInstance);
}
