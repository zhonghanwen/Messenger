package com.ts.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ts.messenger.constant.MessengerConstant;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private Messenger mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
            Message msg = Message.obtain(null, MessengerConstant.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello, this is client");
            msg.setData(bundle);

            msg.replyTo = mGetReplyMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private final class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerConstant.MSG_FROM_SERVICE:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
