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

import nanyibang.com.binder.service.MessagerService;

public class MessengerActivity extends AppCompatActivity {

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

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
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

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MessengerActivity.this, MessagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
