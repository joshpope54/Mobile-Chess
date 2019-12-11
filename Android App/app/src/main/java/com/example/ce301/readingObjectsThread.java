package com.example.ce301;

import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class readingObjectsThread extends Thread {
    private Handler handler;
    private ObjectInputStream objectInputStream;
    private MainActivity activity;

    public readingObjectsThread(MainActivity activity, Handler handler, ObjectInputStream objectInputStream){
        this.handler = handler;
        this.objectInputStream = objectInputStream;
        this.activity = activity;
    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                blahblah();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void blahblah() throws IOException, ClassNotFoundException {
        String success = (String) objectInputStream.readObject();

        String[] items = success.split(" ");
        String[] firstPostion = items[1].split(",");
        String[] secondPosition = items[2].split(",");
        System.out.println(success);
        if(items[0].equalsIgnoreCase("success")){
            //convert position data to array length data
            final int start = (Integer.parseInt(firstPostion[0]) * 8) + Integer.parseInt(firstPostion[1]);
            final int finish = (Integer.parseInt(secondPosition[0]) * 8) + Integer.parseInt(secondPosition[1]);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    TextView view = (TextView) activity.gridLayout.getChildAt(start);
                    TextView view2 = (TextView) activity.gridLayout.getChildAt(finish);
                    view2.setTextColor(view.getCurrentTextColor());
                    view2.setText(view.getText());
                    view2.setTextColor(view.getCurrentTextColor());
                    view.setText("");
                }
            });
        }else if (items[0].equalsIgnoreCase("failure")){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Move Failed, {OTHERCOLOR} move again",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
