package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class Game extends AppCompatActivity {

    private Handler handlerToChessComms; //Used to send messages from main thread to background networking thread.
    private GridLayout gridLayout;
    private int[][] points = {{-1,-1},{-1,-1}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Chess");
        HandlerThread handlerThread = new HandlerThread("ChessHandlerThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Worker worker = new Worker();
        handlerToChessComms = new Handler(looper, worker);
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

    public void checkData() {
        if (points[0][0] != -1 && points[1][0] != -1) {
            handlerToChessComms.handleMessage(new Message());
            handlerToChessComms.post(new Runnable() {
                @Override
                public void run() {
                    //handlerToChessComms.points = points;
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Inform of destruction
    }

    //When closing this acitivity
    //Inform the game server that the thread must be closed.
}
