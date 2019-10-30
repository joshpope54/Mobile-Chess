package com.example.ce301;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView finalResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);

        Button connectButton = (Button) findViewById(R.id.button);
        connectButton.setOnClickListener(this);
        finalResult = findViewById(R.id.moreSoonTextView);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                EditText ip = findViewById(R.id.editText);
                EditText port = findViewById(R.id.editText2);

                String ipString = ip.getText().toString();
                String portString = port.getText().toString();

                RetrieveFeedTask task = new RetrieveFeedTask(this);
                task.execute(ipString, portString);
                break;
        }
    }


    private static class RetrieveFeedTask extends AsyncTask<String, Void, Socket> {
        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveFeedTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }
        @Override
        protected Socket doInBackground(String... strings) {
            InetAddress ipactua = null;
            try {
                ipactua = InetAddress.getByName(strings[0]);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }


            Socket s = null;
            try {
                SocketAddress saddress = new InetSocketAddress(ipactua, Integer.parseInt(strings[1]));
                s = new Socket();
                s.connect(saddress);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return s;
        }

        @Override
        protected void onPostExecute(Socket socket) {
            super.onPostExecute(socket);
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            // modify the activity's UI
            TextView textView = activity.findViewById(R.id.moreSoonTextView);
            textView.setText(socket.toString());
        }
    }
}
