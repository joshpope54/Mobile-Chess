package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ce301.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static android.provider.Telephony.Mms.Part.TEXT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String ipaddress;
    private String server;
    private String matchmaker;
    private Handler handler = new Handler(Looper.getMainLooper());
    // ServerConnection thread;
    public View view1, view2;
    public Dialog dialog;
    public EditText userNameBox;
   // public GameClient gameThread;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USERNAME = "username";
    ServiceClass mService;
    boolean mBound = false;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    private String color;
    Handler editTextHandler = new Handler();
    static class ResponseHandler extends Handler {
        @Override public void handleMessage(Message message) {
            Log.e("message",message.obj.toString());
        }
    }
    Messenger messenger = new Messenger(new ResponseHandler());

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                userNameBox.clearFocus();
                saveUserName();
            }
        }
    };


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
        userNameBox = findViewById(R.id.userNameBox);
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        //EventBus.getDefault().register(this);
        loadUserName();
        userNameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextHandler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            EventBus.getDefault().unregister(this);
            unbindService(connection);
            mBound = false;
        }
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
            if(messageArray[0].equals("CONNECTION_MADE")) {
                //DIALOG CLOSE
                if(userNameBox.getText().toString().equals("")){
                    EventBus.getDefault().post(new EventClass("ACTIVITY", "USERNAME Anonymous"));
                }else{
                    EventBus.getDefault().post(new EventClass("ACTIVITY", "USERNAME " + userNameBox.getText().toString()));
                }
                color = messageArray[1];
            }else if(messageArray[0].equals("ENEMY_NAME")){
                dialog.dismiss();
                unbindService(connection);
                EventBus.getDefault().unregister(this);
                mBound = false;
                Intent dialogIntent = new Intent(this, Game.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogIntent.putExtra("COLOR", color);
                dialogIntent.putExtra("ENEMYNAME", messageArray[1]);
                dialogIntent.putExtra("YOURNAME", userNameBox.getText().toString());
                startActivity(dialogIntent);
            }else if (messageArray[0].equals("DISCONNECTED")){
                unbindService(connection);
                Intent intent = new Intent(this, ServiceClass.class);
                stopService(intent);
                mBound = false;
            }
            Log.e("Message", eventClass.message + " " +eventClass.fromClass);
        }

    }

    public void saveUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, userNameBox.getText().toString());
        editor.apply();
    }

    public void loadUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        userNameBox.setText(sharedPreferences.getString(USERNAME, ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                final Intent intent = new Intent(this, ServiceClass.class);
                startService(intent);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
                EventBus.getDefault().register(this);
                dialog.setCanceledOnTouchOutside(false);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.matchmaking, null);
                dialogView.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "exit"));
                        unbindService(connection);
                        stopService(intent);
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(dialogView);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
