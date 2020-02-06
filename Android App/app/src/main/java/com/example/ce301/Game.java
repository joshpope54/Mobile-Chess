package com.example.ce301;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.example.ce301.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;

//package com.example.ce301.old;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.gridlayout.widget.GridLayout;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.ce301.R;
//
//import java.util.Arrays;
//
public class Game extends AppCompatActivity {
    public GridLayout gridLayout;
    private int[][] points = {{-1, -1}, {-1, -1}};
    ServiceClass mService;
    boolean mBound = false;
    String color;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ServiceClass.LocalBinder binder = (ServiceClass.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Toast.makeText(Game.this, "CONNECTED",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ServiceClass.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(R.layout.activity_game);
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Chess");
        Intent fromintent = getIntent();
        color = fromintent.getStringExtra("COLOR");
        TextView textView = findViewById(R.id.textView70);
        textView.setText(color);
        EventBus.getDefault().register(this);

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

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unbindService(connection);
        mBound = false;
    }

    @Subscribe
    public void onEvent(EventClass eventClass){
        if(!eventClass.fromClass.equals("ACTIVITY")){
            String[] messageArray = eventClass.message.split(" ");
            if(messageArray[0].equals("CONNECTION_MADE")){
                //DIALOG CLOSE
                EventBus.getDefault().post(new EventClass("ACTIVITY", "LEAVE_THREAD"));
                Intent dialogIntent = new Intent(this, Game.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogIntent.putExtra("COLOR", messageArray[1]);
                startActivity(dialogIntent);

            }else if(messageArray[0].equals("PROMOTE")) {
                final Dialog dialog = new Dialog(this);
                dialog.setCanceledOnTouchOutside(false);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.pawn_promotion, null);
                dialogView.findViewById(R.id.cardview1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "PROMOTE_TO QUEEN"));
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.cardview2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "PROMOTE_TO KNIGHT"));
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.cardview3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "PROMOTE_TO ROOK"));
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.cardview4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "PROMOTE_TO BISHOP"));
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            }else if(messageArray[0].equals("PROMOTION")){
                final String[] piecePosition = messageArray[1].split(",");
                final int piecePositionInGrid = (Integer.parseInt(piecePosition[0]) * 8) + Integer.parseInt(piecePosition[1]);
                final String receivedPieceColor = messageArray[2];
                ImageView view2 = (ImageView) gridLayout.getChildAt(piecePositionInGrid);
                switch (messageArray[3]){
                    case "QUEEN":
                        if(receivedPieceColor.equals("WHITE")){
                            view2.setImageResource(R.drawable.chess_qlt60);
                        }else if(receivedPieceColor.equals("BLACK")){
                            view2.setImageResource(R.drawable.chess_qdt60);
                        }
                        break;
                    case "KNIGHT":
                        if(receivedPieceColor.equals("WHITE")){
                            view2.setImageResource(R.drawable.chess_nlt60);
                        }else if(receivedPieceColor.equals("BLACK")){
                            view2.setImageResource(R.drawable.chess_ndt60);
                        }
                        break;

                    case "ROOK":
                        if(receivedPieceColor.equals("WHITE")){
                            view2.setImageResource(R.drawable.chess_rlt60);
                        }else if(receivedPieceColor.equals("BLACK")) {
                            view2.setImageResource(R.drawable.chess_rdt60);
                        }
                        break;
                    case "BISHOP":
                        if(receivedPieceColor.equals("WHITE")){
                            view2.setImageResource(R.drawable.chess_blt60);
                        }else if(receivedPieceColor.equals("BLACK")){
                            view2.setImageResource(R.drawable.chess_bdt60);
                        }
                        break;
                }
            }else if(messageArray[0].equals("SUCCESS")){
                final String[] firstPostion = messageArray[1].split(",");
                final String[] secondPosition = messageArray[2].split(",");
                final int start = (Integer.parseInt(firstPostion[0]) * 8) + Integer.parseInt(firstPostion[1]);
                final int finish = (Integer.parseInt(secondPosition[0]) * 8) + Integer.parseInt(secondPosition[1]);
                TextView lastMove = (TextView) findViewById(R.id.textView67);
                lastMove.setText(Arrays.toString(firstPostion) + " To " + Arrays.toString(secondPosition));
                ImageView view = (ImageView) gridLayout.getChildAt(start);
                ImageView view2 = (ImageView) gridLayout.getChildAt(finish);
                view2.setImageDrawable(view.getDrawable());
                view.setImageResource(android.R.color.transparent);
                //DIALOG CLOSE
            }else if(messageArray[0].equals("FAILURE")){
                Toast.makeText(this, "MOVE FAILURE > MOVE FORFEITED", Toast.LENGTH_LONG).show();
            }

                Log.e("Message", eventClass.message + " " +eventClass.fromClass);
        }

    }

    public void checkData() {
        if (points[0][0] != -1 && points[1][0] != -1) {
            EventBus.getDefault().post(new EventClass("ACTIVITY", color, points));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Inform of destruction
    }
//
//    //When closing this acitivity
//    //Inform the game server that the thread must be closed.
}
