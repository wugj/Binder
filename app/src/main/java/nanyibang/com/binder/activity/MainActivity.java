package nanyibang.com.binder.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import nanyibang.com.binder.Constants;
import nanyibang.com.binder.R;
import nanyibang.com.binder.aidl.IBinderPool;
import nanyibang.com.binder.aidl.ICompute;
import nanyibang.com.binder.aidl.ISecurityCenter;
import nanyibang.com.binder.service.IBinderPoolService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ISecurityCenter mIsecurityCenter;
    private ICompute mIcompute;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBinderPool iBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                IBinder iBinder = iBinderPool.queryBinder(Constants.BINDER_COMPUTE);
                mIcompute = ICompute.Stub.asInterface(iBinder);
                mIsecurityCenter = ISecurityCenter.Stub.asInterface(iBinderPool.queryBinder(Constants.BINDER_SECURITY_CENTER));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: 开始链接");
        Intent intent = new Intent(MainActivity.this, IBinderPoolService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isEn;

    public void clickSecurityCenter(View view){
        if (isEn){
            isEn = false;
            try {
                String word = mIsecurityCenter.encrypt("word");
                Log.e(TAG, "clickSecurityCenter: 加密 : " + word);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            isEn = true;
            try {
                String hellow = mIsecurityCenter.decrypt("hellow");
                Log.e(TAG, "clickSecurityCenter: 解密 : " + hellow);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }
    private int count;
    public void clickPlus(View view){
        count++;
        Log.e(TAG, "clickPlus: count" + count);
    }
}
