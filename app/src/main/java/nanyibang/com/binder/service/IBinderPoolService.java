package nanyibang.com.binder.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import nanyibang.com.binder.Constants;
import nanyibang.com.binder.aidl.ComputeImpl;
import nanyibang.com.binder.aidl.IBinderPool;
import nanyibang.com.binder.aidl.SecurityCenterImpl;

public class IBinderPoolService extends Service {

    private static final String TAG = "IBinderPoolService";

    private IBinder mIBinderPool = new IBinderPool.Stub(){

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            Log.e(TAG, "queryBinder: 开始查询知道Binder");
            switch (binderCode){
                case Constants.BINDER_SECURITY_CENTER:
                    return new SecurityCenterImpl();
                case Constants.BINDER_COMPUTE:
                    return new ComputeImpl();
            }
            return null;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinderPool;
    }
}
