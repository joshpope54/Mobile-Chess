package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

public class Game extends AppCompatActivity {

    private String color;
    private int[][] points = {{-1,-1},{-1,-1}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Chess");
        final GridLayout gridLayout = findViewById(R.id.gridlayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            TextView textView = (TextView)gridLayout.getChildAt(i);
            final int row_no=i/8;
            final int col_no=i%8;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkData();
                    if(points[0][0]==-1){
                        points[0][0]= row_no;
                        points[0][1]= col_no;
                    }else if (points[1][0]==-1){
                        points[1][0]= row_no;
                        points[1][1]= col_no;
                    }

                    Log.e("Points", ""+ Arrays.toString(points[0]));
                    Log.e("Points", ""+ Arrays.toString(points[1]));
                }
            });


        }
    }

    public void checkData(){
        if(points[0][0]!=-1 && points[1][0]!=-1){
            points[0][0] = -1;
            points[0][1] = -1;
            points[1][0] = -1;
            points[1][1] = -1;
        }
    }

    //IDEA
    //TAP INITIAL TEXT VIEW
    //GET POINT (eg 0,0)
    //


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Inform of destruction
    }

    //When closing this acitivity
    //Inform the game server that the thread must be closed.
}
