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


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ConnectionThread extends Thread{
    private String ip;
    private String port;
    private final Handler handler;
    private Activity activity;
    private Socket s;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    public String communication = "";
    public boolean connected;



    public ConnectionThread(String ip, String port, Handler handler, Activity activity){
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        this.activity = activity;
    }

    @Override
    public void run() {
        super.run();
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
            connected = true;
            dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream = new DataOutputStream(s.getOutputStream());

            while (true) {
                if(communication.equalsIgnoreCase("exit")){
                    try {
                        dataOutputStream.writeUTF("exit");
                        System.out.println("socket being closed?");
                        s.close();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            dataInputStream.close();
            dataOutputStream.close();

        } catch (SocketTimeoutException e) {
            connected = false;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
