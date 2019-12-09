package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Game extends AppCompatActivity {


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
                    System.out.println("Item Clicked" + row_no + "" + col_no);
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
