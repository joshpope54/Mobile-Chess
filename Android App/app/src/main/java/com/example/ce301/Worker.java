package com.example.ce301.new_pack;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker implements Handler.Callback {

    protected Socket socket;
    protected PrintWriter writer;

    public Worker() throws Exception{

    }

    public PrintWriter getWriter(){
        return this.writer;
    }

    public Socket getSocket(){
        return this.socket;
    }
    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (socket == null){
            try {
                this.socket = new Socket("100.86.213.7", 5561);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("HELLOHELLOHELLOHELLOHELLOHELLO");

        return false;
    }
}
