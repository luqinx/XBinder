/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.luqinx.xbinder;

import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;

public interface ChannelBinder extends android.os.IInterface {

    /**
     * Local-side IPC implementation stub class.
     */
    abstract class Stub extends android.os.Binder implements ChannelBinder {
        private static final java.lang.String DESCRIPTOR = "BinderChannel";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an BinderChannel interface,
         * generating a proxy if needed.
         */
        public static ChannelBinder asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin instanceof ChannelBinder))) {
                return ((ChannelBinder) iin);
            }
            return new ChannelBinder.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_invokeMethod: {
                    data.enforceInterface(descriptor);
                    ChannelArgument _arg0 = new ChannelArgument(data);
                    ChannelResult _result = null;
                    if (_arg0.asyncCall) {
                        XBinderExecutor.INSTANCE.executeAsyncCall(() -> {
                            try {
                                invokeMethod(_arg0);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        _result = invokeMethod(_arg0);
                    }
                    if (!_arg0.onewayCall) {
                        reply.writeNoException();
                        try {
                          ChannelArgument.Companion.setToProcess(_arg0.fromProcess);
                          if ((_result != null)) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                          } else {
                            reply.writeInt(0);
                          }
                        } finally {
                            ChannelArgument.Companion.removeFromThreadLocal();
                        }
                    }

                    return true;
                }
                case TRANSACTION_registerCallbackBinder:
                    data.enforceInterface(descriptor);
                    this.registerCallbackChannel(data.readString(),
                            ChannelBinder.Stub.asInterface(data.readStrongBinder())
                    );
                    return true;
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements ChannelBinder {
            private final android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public ChannelResult invokeMethod(@NonNull ChannelArgument rpcArgument) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                ChannelResult _result = null;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    rpcArgument.writeToParcel(_data, 0);
                    int flags = rpcArgument.onewayCall ? IBinder.FLAG_ONEWAY : 0;
                    boolean _status = mRemote.transact(Stub.TRANSACTION_invokeMethod, _data, _reply, flags);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().invokeMethod(rpcArgument);
                    }
                    if (flags != IBinder.FLAG_ONEWAY) {
                        _reply.readException();
                        if ((0 != _reply.readInt())) {
                            _result = new ChannelResult(_reply);
                        }
                    } else {
                        // oneway
                        _result = new ChannelResult();
                        _result.setSucceed(_status);
                        _result.setInvokeConsumer(0L);
                        if (!_status) {
                            _result.setErrCode(BinderInvoker.ERROR_CODE_ONEWAY_ERROR);
                            _result.setErrMessage("oneway failed!");
                        }
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void registerCallbackChannel(String process, ChannelBinder callbackService) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(process);
                    _data.writeStrongBinder(callbackService.asBinder());
                    boolean _status = mRemote.transact(Stub.TRANSACTION_registerCallbackBinder, _data, null, android.os.IBinder.FLAG_ONEWAY);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().registerCallbackChannel(process, callbackService);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public static ChannelBinder sDefaultImpl;
        }

        static final int TRANSACTION_invokeMethod = (android.os.IBinder.FIRST_CALL_TRANSACTION);
        static final int TRANSACTION_registerCallbackBinder = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);

        public static ChannelBinder getDefaultImpl() {
            return Stub.Proxy.sDefaultImpl;
        }
    }

    ChannelResult invokeMethod(ChannelArgument rpcArgument) throws android.os.RemoteException;

    void registerCallbackChannel(String process, ChannelBinder channel) throws android.os.RemoteException;
}
