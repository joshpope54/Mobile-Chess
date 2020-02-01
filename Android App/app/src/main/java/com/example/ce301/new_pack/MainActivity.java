package com.example.ce301.new_pack;

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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ce301.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ipaddress = "100.86.213.7";//"joshpope.dev";
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, ServiceClass.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:

                Toast.makeText(this, ""+mService.getRandomNumber(),Toast.LENGTH_SHORT).show();
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
