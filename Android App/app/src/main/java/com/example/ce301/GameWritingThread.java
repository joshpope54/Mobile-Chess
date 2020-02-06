package com.example.ce301;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameWritingThread extends Thread {
    public PrintWriter printWriter;
    private Handler serviceHandler;
    public static EventClass externalMessage = null;
    private boolean running = true;

    public GameWritingThread(Handler handler, PrintWriter writer) {
        this.serviceHandler = handler;
        this.printWriter = writer;
    }

    @Override
    public void run() {
        super.run();
        while (running){
            if(externalMessage != null){
                if(externalMessage.points!=null){
                    printWriter.println(externalMessage.message+" "+externalMessage.points[0][0]+","+externalMessage.points[0][1]+" "+externalMessage.points[1][0]+","+externalMessage.points[1][1]);
                }else{
                    if(externalMessage.message.equals("exit")){
                        running=false;
                    }
                    printWriter.println(externalMessage.message);
                }
                externalMessage = null;
            }
        }
    }
}
