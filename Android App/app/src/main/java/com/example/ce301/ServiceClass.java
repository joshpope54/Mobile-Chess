package com.example.ce301;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.Random;

public class ServiceClass extends Service {
    static class LocalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            EventBus.getDefault().post(new EventClass("SERVICE", msg.obj.toString()));
        }
    }
    protected Handler handler;
    private final IBinder binder = new LocalBinder();
    GameReadingThread thread;
    public class LocalBinder extends Binder {
        ServiceClass getService() {
            return ServiceClass.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        this.handler = new LocalHandler();
        thread = new GameReadingThread(this.handler);
        thread.start();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(EventClass eventClass){
        if(!eventClass.fromClass.equals("SERVICE")){
            Log.e("Message", eventClass.message + " " +eventClass.fromClass);
            GameWritingThread.externalMessage = eventClass;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void sendMessageToService(String message){
        Log.e("MessageFromBoundAct", message);
    }

}
