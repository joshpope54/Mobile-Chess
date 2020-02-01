package com.example.ce301.new_pack;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Random;

public class ServiceClass extends Service {
    private Looper serviceLooper;
    private Handler serviceHandler;

    // Handler that receives messages from the thread
    private final IBinder binder = new LocalBinder();
    private final Random mGenerator = new Random();

    public class LocalBinder extends Binder {
        ServiceClass getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServiceClass.this;
        }
    }

    //WANT
    //: BOOUND SERVICE
    //: HOW TO BIND TO MULTIPLE ACTIVITIES


    @Override
    public void onCreate() {
        super.onCreate();
        GameThread thread = new GameThread();
        thread.start();
        Log.e("tag", "HELLO ON CREATE CALLED");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return binder;
    }

    public void sendMessageToServer(){

    }

    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
}
