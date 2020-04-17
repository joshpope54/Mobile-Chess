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
                    if(externalMessage.message.equals("BLACK")){
                        int gridPositionStart0 = Math.abs(externalMessage.points[0][0] - 7);
                        int gridPositionStart1 = Math.abs(7 - externalMessage.points[0][1]);
                        int gridPositionFinish0 = Math.abs(externalMessage.points[1][0] - 7);
                        int gridPositionFinish1 = Math.abs(7 - externalMessage.points[1][1]);
                        printWriter.println(externalMessage.message+" "+gridPositionStart0+","+gridPositionStart1+" "+gridPositionFinish0+","+gridPositionFinish1);

                    }else{
                        printWriter.println(externalMessage.message+" "+externalMessage.points[0][0]+","+externalMessage.points[0][1]+" "+externalMessage.points[1][0]+","+externalMessage.points[1][1]);
                    }
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
