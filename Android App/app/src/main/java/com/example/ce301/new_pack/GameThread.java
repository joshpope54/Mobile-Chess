package com.example.ce301.new_pack;

import android.icu.text.ScientificNumberFormatter;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameThread extends Thread {
    private Socket socket;
    private PrintWriter printWriter;
    private Scanner scanner;

    public GameThread() {
    }

    @Override
    public void run() {
        super.run();
        String ip = "100.86.213.7";
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
            printWriter = new PrintWriter(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());

            while (true){
                String input = scanner.nextLine();
                System.out.println(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
