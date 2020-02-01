//package com.example.ce301.old;
//
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Looper;
//
//
//import com.example.ce301.new_pack.MainActivity;
//import com.example.ce301.old.Game;
//
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
//public class GameClient extends Thread{
//    private String ip;
//    private String port;
//    private final Handler handler;
//    private MainActivity activity;
//    private Socket s;
//    private Scanner objectInputStream;
//    private PrintWriter dataOutputStream;
//    private boolean inGame;
//    public int[][] points;
//    public Handler currenthandler;
//    String color;
//    boolean running = true;
//    public Game gameActivity;
//
//
//
//    public GameClient(String ip, String port, MainActivity activity){
//        this.ip = ip;
//        this.port = port;
//        this.activity = activity;
//        handler = new Handler(Looper.getMainLooper());
//        currenthandler = new Handler(Looper.myLooper());
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
//
//        try {
//            SocketAddress saddress = new InetSocketAddress(ipactua, Integer.parseInt(port));
//            s = new Socket();
//            s.connect(saddress, 2000);
//            objectInputStream = new Scanner(s.getInputStream());
//            dataOutputStream = new PrintWriter(s.getOutputStream(),true);
//
//            while (running) {
//                if (!inGame) {
//                    String recieved = objectInputStream.nextLine();
//                    if (recieved.equalsIgnoreCase("connected")) {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent newIntent = new Intent(activity, Game.class);
//                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.getApplication().startActivity(newIntent);
//                            }
//                        });
//                        inGame = true;
//                        String recievedString = objectInputStream.nextLine();
//                        color = recievedString;
////                        handler.post(new Runnable() {
////                            @Override
////                            public void run() {
////                                TextView colorview = activity.findViewById(R.id.textView70);
////                                colorview.setText(color);
////                            }
////                        });
////                        readingObjectsThread readingThread = new readingObjectsThread(activity, handler, objectInputStream, dataOutputStream);
////                        readingThread.start();
//                    }
//                }
//
//                if (inGame) {
//                    if (points != null) {
//                        dataOutputStream.println(color + " " + points[0][0] + "," + points[0][1] + " " + points[1][0] + "," + points[1][1]);
//                        points = null;
//                    }
//                }
//            }
//        } catch (SocketTimeoutException e) {
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
