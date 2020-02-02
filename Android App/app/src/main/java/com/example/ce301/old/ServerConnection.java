//package com.example.ce301.old;
//
//import android.app.ActionBar;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.TextView;
//
//
//import com.example.ce301.R;
//import com.example.ce301.new_pack.MainActivity;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketAddress;
//import java.net.SocketTimeoutException;
//import java.net.UnknownHostException;
//import java.util.Scanner;
//
//public class ServerConnection extends Thread{
//    private String ip;
//    private String port;
//    private final Handler handler;
//    private MainActivity activity;
//    private Socket s;
//    private Scanner dataInputStream;
//    private PrintWriter dataOutputStream;
//    public Handler serverConnectionHandler;
//
//
//
//    public ServerConnection(String ip, String port, Handler handler, MainActivity activity){
//        this.ip = ip;
//        this.port = port;
//        this.handler = handler;
//        this.activity = activity;
//        serverConnectionHandler = new Handler(Looper.myLooper());
//    }
//
//    @Override
//    public void run() {
//        super.run();
//        InetAddress ipactua = null;
//        try {
//            ipactua = InetAddress.getByName(ip);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        try {
//            SocketAddress saddress = new InetSocketAddress(ipactua, Integer.parseInt(port));
//            s = new Socket();
//            s.connect(saddress, 2000);
//            dataOutputStream = new PrintWriter(s.getOutputStream(), true);
//            dataInputStream = new Scanner(s.getInputStream());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
