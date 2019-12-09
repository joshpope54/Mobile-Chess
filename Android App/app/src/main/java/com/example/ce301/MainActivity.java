package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView finalResult;
    private String ipaddress;
    private String server;
    private String matchmaker;
    private Handler handler = new Handler();
    ConnectionThread thread;
    private View view1, view2;
    public Dialog dialog;
    public GameClient gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ipaddress = "100.92.33.130";//getString(R.string.ip_address);
        server = getString(R.string.server_port);
        matchmaker = getString(R.string.matchmaker_port);
        thread = new ConnectionThread(ipaddress, server, handler, this);
        thread.start();
        dialog = new Dialog(this);
        view1 = getLayoutInflater().inflate(R.layout.activity_main, null);
        view2 = getLayoutInflater().inflate(R.layout.not_connected, null);
        //setContentView(view2);


        if (thread.isAlive()) {
            setContentView(view1);
            androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar1);
            setSupportActionBar(myToolbar);
            Button playButton = findViewById(R.id.playButton);
            playButton.setOnClickListener(this);
        } else {
            setContentView(view2);
        }


    }

    @Override
    protected void onDestroy() {
        thread.communication = "exit";
        //gameThread.communication="exit";
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                if (thread.isAlive()) {
                    gameThread = new GameClient(ipaddress, matchmaker, handler, this);
                    gameThread.start();
                    //Thread Started
                    //Open Dialog
                    //Wait for Player - Tell this through the socket
                    //Load new activity if player found

                    dialog.setCanceledOnTouchOutside(false);

                    View dialogView = LayoutInflater.from(this).inflate(R.layout.matchmaking, null);
                    dialogView.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            gameThread.closeConnection();
                            //Tell the Communication thread to remove us from the Queue
                            //requires handler
                            //how to tell the thread?
                        }
                    });

                    dialog.setContentView(dialogView);
                    dialog.show();

                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //}
                    break;

                }
        }
    }
}
