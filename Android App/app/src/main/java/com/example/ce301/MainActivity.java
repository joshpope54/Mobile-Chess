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

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.spec.ECField;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView finalResult;
    private String ipaddress;
    private String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);

        Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        ipaddress = getString(R.string.ip_address);
        port = getString(R.string.port_number);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.playButton:

//                RetrieveFeedTask task = new RetrieveFeedTask(this);
//                task.execute(ipaddress, port);

                //Open dialog
                //progress spinner
                //waiting for player (online count)

                
                break;
        }
    }


    private static class RetrieveFeedTask extends AsyncTask<String, Void, AsyncTaskResult<Socket>> {
        private WeakReference<MainActivity> activityReference;
        private Socket socket;


        // only retain a weak reference to the activity
        RetrieveFeedTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }
        @Override
        protected AsyncTaskResult<Socket> doInBackground(String... strings) {
            InetAddress ipactua = null;
            try {
                ipactua = InetAddress.getByName(strings[0]);
            } catch (UnknownHostException e) {
                return new AsyncTaskResult<>(e);
            }


            Socket s = null;
            try {
                SocketAddress saddress = new InetSocketAddress(ipactua, Integer.parseInt(strings[1]));
                s = new Socket();
                s.connect(saddress, 2000);
            } catch (SocketTimeoutException e) {
                return new AsyncTaskResult<>(e);
            } catch (IOException e) {
                return new AsyncTaskResult<>(e);
            }
            return new AsyncTaskResult<>(s);
        }


        @Override
        protected void onPostExecute(AsyncTaskResult<Socket> taskResult) {
            super.onPostExecute(taskResult);
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            if(taskResult.getError() != null){
            }else if (isCancelled()){
                //canceled
            }else{
                // modify the activity's UI
                socket = taskResult.getResult();
            }
        }
    }
}
