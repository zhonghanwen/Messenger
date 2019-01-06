package com.ts.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.ts.messenger.constant.MessengerConstant;

public class MessengerService extends Service {

    public static final String TAG = MessengerHandler.class.getSimpleName();

    public static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerConstant
                        .MSG_FROM_CLIENT:
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, MessengerConstant.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("replay", "嗯，你的消息我已经收到，稍后会回复你。");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());
}
