package nanyibang.com.binder.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import nanyibang.com.binder.Constants;

public class MessagerService extends Service {

    private static final String TAG = "MessagerService";

    private class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.MSG_FROM_CLIENT:
                    Log.e(TAG, "handleMessage: " + msg.getData().getString("msg"));
                    Message message = Message.obtain(null, Constants.MSG_FROM_SERVER);
                    Messenger messenger = msg.replyTo;
                    if (messenger != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", "嗯 好  这里是服务器");
                        message.setData(bundle);
                        try {
                            messenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    default:
                        super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    public MessagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
