package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView finalResult;
    private String ipaddress;
    private String server;
    private String matchmaker;
    private Handler handler = new Handler(Looper.getMainLooper());
    ConnectionThread thread;
    private View view1, view2, view3;
    public Dialog dialog;
    public GameClient gameThread;
    public GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //remove and replace with proper thread handling
        StrictMode.setThreadPolicy(policy);
        ipaddress = "100.86.213.17";
        server = getString(R.string.server_port);
        matchmaker = getString(R.string.matchmaker_port);
        thread = new ConnectionThread(ipaddress, server, handler, this);
        thread.start();
        dialog = new Dialog(this);
        view1 = getLayoutInflater().inflate(R.layout.activity_main, null);
        view2 = getLayoutInflater().inflate(R.layout.not_connected, null);
        view3 = getLayoutInflater().inflate(R.layout.activity_game, null);

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
    private String color;
    private int[][] points = {{-1,-1},{-1,-1}};

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        gridLayout = findViewById(R.id.gridlayout);
        if(gridLayout!=null){
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                ImageView imageView = (ImageView)gridLayout.getChildAt(i);
                final int row_no=i/8;
                final int col_no=i%8;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(points[1][1]!=-1){
                            points[0][0] = -1;
                            points[0][1] = -1;
                            points[1][0] = -1;
                            points[1][1] = -1;
                        }

                        if(points[0][0]==-1){
                            points[0][0]= row_no;
                            points[0][1]= col_no;
                        }else if (points[1][0]==-1){
                            points[1][0]= row_no;
                            points[1][1]= col_no;
                            checkData();
                        }

                        Log.e("Points", ""+ Arrays.toString(points[0]));
                        Log.e("Points", ""+ Arrays.toString(points[1]));
                    }
                });

            }
        }
    }

    public void checkData(){
        if(points[0][0]!=-1 && points[1][0]!=-1){
            gameThread.currenthandler.post(new Runnable() {
                @Override
                public void run() {
                    gameThread.points = points;
                }
            });
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
