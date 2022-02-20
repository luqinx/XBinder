/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.luqinx.xbinder;

import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;

public interface BinderChannelService extends android.os.IInterface
{
  /** Default implementation for IPCService. */
  public static class Default implements BinderChannelService
  {
    @Override public ChannelMethodResult invokeMethod(ChannelMethodArgument rpcArgument) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void unRegisterCallbackMethod(java.lang.String fromProcess, java.lang.String methodId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements BinderChannelService
  {
    private static final java.lang.String DESCRIPTOR = "IPCService";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an IPCService interface,
     * generating a proxy if needed.
     */
    public static BinderChannelService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof BinderChannelService))) {
        return ((BinderChannelService)iin);
      }
      return new BinderChannelService.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_invokeMethod:
        {
          data.enforceInterface(descriptor);
          ChannelMethodArgument _arg0 = ChannelMethodArgument.CREATOR.createFromParcel(data);
          ChannelMethodResult _result = null;
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
            if ((_result != null)) {
              reply.writeInt(1);
              _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            } else {
              reply.writeInt(0);
            }
          }

          return true;
        }
        case TRANSACTION_unRegisterCallbackMethod:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.unRegisterCallbackMethod(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements BinderChannelService
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public ChannelMethodResult invokeMethod(@NonNull ChannelMethodArgument rpcArgument) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        ChannelMethodResult _result = null;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          rpcArgument.writeToParcel(_data, 0);
          int flags = rpcArgument.onewayCall ? IBinder.FLAG_ONEWAY: 0;
          boolean _status = mRemote.transact(Stub.TRANSACTION_invokeMethod, _data, _reply, flags);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().invokeMethod(rpcArgument);
          }
          if (flags != IBinder.FLAG_ONEWAY) {
            _reply.readException();
            if ((0 != _reply.readInt())) {
              _result = ChannelMethodResult.CREATOR.createFromParcel(_reply);
            }
          } else {
            // oneway
            _result = new ChannelMethodResult();
            _result.setSucceed(_status);
            _result.setInvokeConsumer(0L);
            if (!_status) {
              _result.setErrCode(BinderInvoker.ERROR_CODE_ONEWAY_ERROR);
              _result.setErrMessage("oneway failed!");
            }
          }

        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }

      @Override public void unRegisterCallbackMethod(java.lang.String fromProcess, java.lang.String methodId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(fromProcess);
          _data.writeString(methodId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterCallbackMethod, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
//            getDefaultImpl().unRegisterCallbackMethod(fromProcess, methodId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static BinderChannelService sDefaultImpl;
    }
    static final int TRANSACTION_invokeMethod = (android.os.IBinder.FIRST_CALL_TRANSACTION);
    static final int TRANSACTION_unRegisterCallbackMethod = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(BinderChannelService impl) {
      // Only one user of this interface can use this function
      // at a time. This is a heuristic to detect if two different
      // users in the same process use this function.
      if (Stub.Proxy.sDefaultImpl != null) {
        throw new IllegalStateException("setDefaultImpl() called twice");
      }
      if (impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static BinderChannelService getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public ChannelMethodResult invokeMethod(ChannelMethodArgument rpcArgument) throws android.os.RemoteException;
//  public ParcelableResult invokeCallbackMethod(IPCallbackMethod ipcMethod) throws android.os.RemoteException;
  public void unRegisterCallbackMethod(java.lang.String fromProcess, java.lang.String methodId) throws android.os.RemoteException;
}
