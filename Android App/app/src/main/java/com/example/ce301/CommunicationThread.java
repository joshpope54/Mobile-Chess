package com.example.ce301;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class CommunicationThread extends Thread{
    private String ip;
    private String port;
    private final Handler handler;
    private Activity activity;


    public CommunicationThread(String ip, String port, Handler handler, Activity activity){
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        this.activity = activity;
    }

    @Override
    public void run() {
        super.run();
        Socket s = null;
        InetAddress ipactua = null;
        try {
            ipactua = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            SocketAddress saddress = new InetSocketAddress(ipactua, Integer.parseInt(port));
            s = new Socket();
            s.connect(saddress, 2000);

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Socket finalS = s;




    }
}
