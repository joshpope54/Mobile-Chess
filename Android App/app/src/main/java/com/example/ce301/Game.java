package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Game extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Chess");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Inform of destruction
    }

    //When closing this acitivity
    //Inform the game server that the thread must be closed.
}
