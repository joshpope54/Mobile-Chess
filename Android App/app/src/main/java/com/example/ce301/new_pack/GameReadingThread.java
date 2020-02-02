package com.example.ce301.new_pack;

import android.icu.text.ScientificNumberFormatter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameReadingThread extends Thread {
    private Socket socket;
    public PrintWriter printWriter;
    public Scanner scanner;
    private Handler serviceHandler;
    //public LocalHandler gameHandler;
    public String externalMessage = "";
    public GameWritingThread writingThread;

    public GameReadingThread(Handler handler) {
        this.serviceHandler = handler;
    }
//    static class LocalHandler extends Handler {
//        GameWritingThread thread;
//        public LocalHandler(GameWritingThread thread){
//            this.thread = thread;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            thread.externalMessage = msg.obj.toString();
//        }
//    }
    @Override
    public void run() {
        super.run();
        String ip = "100.86.213.35";
        InetAddress ipactua = null;
        try {
            ipactua = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            SocketAddress saddress = new InetSocketAddress(ipactua, Integer.parseInt("5560"));
            socket = new Socket();
            socket.connect(saddress, 2000);
            Log.e("socket", socket.toString());
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(socket.getInputStream());
            writingThread = new GameWritingThread(serviceHandler, printWriter);
            writingThread.start();

            while (true){
                String input = scanner.nextLine();
                Message message = serviceHandler.obtainMessage();
                message.obj = input;
                this.serviceHandler.sendMessage(message);
            }
        } catch (Exception e) {
            Log.e("Server:", "DISCONNECTED");
        }finally {
            try {
                scanner.close();
                printWriter.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
