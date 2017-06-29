package nanyibang.com.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

import nanyibang.com.binder.aidl.Book;
import nanyibang.com.binder.aidl.IBookManager;
import nanyibang.com.binder.aidl.IOnNewBookArrivedListener;
import nanyibang.com.binder.service.BookManagerService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private class ReplayMessageHanler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_SERVER:
                    Log.e(TAG, "handleMessage: " + msg.getData().get("msg") );
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger mReplayMessenger = new Messenger(new ReplayMessageHanler());

    private IOnNewBookArrivedListener mListener = new IOnNewBookArrivedListener.Stub(){

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Log.e(TAG, "onNewBookArrived: " + book.toString() );
        }
    };

    private IBookManager mBookManager;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: 链接成功");
            if (service == null) {
                Log.e(TAG, "onServiceConnected: 链接service为null  连接失败");
            }
            mBookManager = IBookManager.Stub.asInterface(service);
            try {
                Log.e(TAG, "onServiceConnected: listener对象： " + mListener);
                mBookManager.registerListener(mListener);
                List<Book> bookList = mBookManager.getBookList();
                Log.e(TAG, "onServiceConnected: " + bookList.getClass().getCanonicalName() );
                Log.e(TAG, "onServiceConnected: " + bookList.toString());
                mBookManager.addBook(new Book(3, "wp"));
                Log.e(TAG, "onServiceConnected: " + mBookManager.getBookList());

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Messenger messenger = new Messenger(service);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "哈喽啊， 这是客户端");
            Message message = Message.obtain(null, Constants.MSG_FROM_CLIENT);
            message.setData(bundle);
            message.replyTo = mReplayMessenger;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceConnected: 链接失败 : " + name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: 开始链接");
        Intent intent = new Intent(MainActivity.this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        if (mListener != null && mBookManager.asBinder().isBinderAlive()){
            try {
                Log.e(TAG, "onStop: 开始注销对象" + mListener );
                mBookManager.unregisterListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);

        super.onDestroy();
    }

    public void click(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Book> bookList = mBookManager.getBookList();
                    Log.e(TAG, "click: " + bookList.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
