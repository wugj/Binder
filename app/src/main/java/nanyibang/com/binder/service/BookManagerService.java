package nanyibang.com.binder.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import nanyibang.com.binder.aidl.Book;
import nanyibang.com.binder.aidl.IBookManager;
import nanyibang.com.binder.aidl.IOnNewBookArrivedListener;

/**
 * @name: nanyibang.com.binder.service
 * @description:
 * @author：Administrator
 * @date: 2017-06-28 19:06
 * @company: 上海若美科技有限公司
 */

public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mOnNewBookArrivedListenerList = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub(){

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            onNewBookArrived(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.e(TAG, "onNewBookArrived: 注册的listener： " + listener );
            mOnNewBookArrivedListenerList.register(listener);
            Log.e(TAG, "registerListener: size： " + mOnNewBookArrivedListenerList.beginBroadcast() );
            mOnNewBookArrivedListenerList.finishBroadcast();
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mOnNewBookArrivedListenerList.unregister(listener);
            Log.e(TAG, "unregisterListener: size： " + mOnNewBookArrivedListenerList.beginBroadcast() );
            mOnNewBookArrivedListenerList.finishBroadcast();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "android"));
        mBookList.add(new Book(2, "ios"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int i = checkCallingOrSelfPermission("com.binder.test");
        if (i == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "onBind: 授权验证失败");
            return null;
        }
        Log.e(TAG, "onBind: 授权验证通过");
        return mBinder;
    }

    private void onNewBookArrived(Book book){
        mBookList.add(book);
        final int count = mOnNewBookArrivedListenerList.beginBroadcast();
        for (int i = 0; i < count; i++){
            IOnNewBookArrivedListener item = mOnNewBookArrivedListenerList.getBroadcastItem(i);
            if (item != null) {
                try {
                    item.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mOnNewBookArrivedListenerList.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = mBookList.size() + 1;
                onNewBookArrived(new Book(i , "new Book#" + i));
            }
        }
    }

}
