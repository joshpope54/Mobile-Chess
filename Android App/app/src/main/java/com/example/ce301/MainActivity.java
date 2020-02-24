package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.ce301.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String ipaddress;
    private String server;
    private String matchmaker;
    private Handler handler = new Handler(Looper.getMainLooper());
    // ServerConnection thread;
    public View view1, view2;
    public Dialog dialog;
   // public GameClient gameThread;

    ServiceClass mService;
    boolean mBound = false;

    static class ResponseHandler extends Handler {
        @Override public void handleMessage(Message message) {
            Log.e("message",message.obj.toString());
        }
    }
    Messenger messenger = new Messenger(new ResponseHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //remove and replace with proper thread handling
        //StrictMode.setThreadPolicy(policy);
        ipaddress = "joshpope.dev";//"joshpope.dev";
        server = getString(R.string.server_port);
        matchmaker = getString(R.string.matchmaker_port);
        dialog = new Dialog(this);
        view1 = getLayoutInflater().inflate(R.layout.activity_main, null);
        view2 = getLayoutInflater().inflate(R.layout.not_connected, null);
        setContentView(view1);
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unbindService(connection);
        mBound = false;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServiceClass.LocalBinder binder = (ServiceClass.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Subscribe
    public void onEvent(EventClass eventClass){
        if(!eventClass.fromClass.equals("ACTIVITY")){
            String[] messageArray = eventClass.message.split(" ");
            if(messageArray[0].equals("CONNECTION_MADE")){
                //DIALOG CLOSE
                dialog.dismiss();
                Intent dialogIntent = new Intent(this, Game.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogIntent.putExtra("COLOR", messageArray[1]);
                startActivity(dialogIntent);
            }else if (messageArray[0].equals("DISCONNECTED")){
                unbindService(connection);
                mBound = false;
            }
            Log.e("Message", eventClass.message + " " +eventClass.fromClass);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                Intent intent = new Intent(this, ServiceClass.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
                dialog.setCanceledOnTouchOutside(false);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.matchmaking, null);
                dialogView.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "exit"));
                        dialog.dismiss();

                        //Tell the Communication thread to remove us from the Queue
                        //requires handler
                        //how to tell the thread?
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();

                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);



                //Toast.makeText(this, ""+mService.getRandomNumber(),Toast.LENGTH_SHORT).show();
                //gameThread = new GameClient(ipaddress, matchmaker, this);
                //gameThread.start();


//                if (thread.isAlive()) {

//                    dialog.setCanceledOnTouchOutside(false);
//                    View dialogView = LayoutInflater.from(this).inflate(R.layout.matchmaking, null);
//                    dialogView.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                            gameThread.currenthandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    gameThread.closeConnection();
//                                }
//                            });
//                        }
//                    });
//                    dialog.setContentView(dialogView);
//                    dialog.show();
//                    Window window = dialog.getWindow();
//                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    break;
//
//                }
        }
    }


}
