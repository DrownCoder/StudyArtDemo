package com.study.studyartdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.study.studyartdemo.app.Constants;

/**
 * Created by dengzhaoxuan on 2017/4/24.
 */

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_CLIENT:
                    Log.i(TAG, "receive msg from Client:" + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null,Constants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","reply msg from Service to Client.");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    private final Messenger mMessenger = new Messenger(new MessengerHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
