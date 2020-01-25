package com.example.ce301;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class readingObjectsThread extends Thread {
    private Handler handler;
    private ObjectInputStream objectInputStream;
    private MainActivity activity;
    private DataOutputStream dataOutputStream;

    public readingObjectsThread(MainActivity activity, Handler handler, ObjectInputStream objectInputStream, DataOutputStream dataOutputStream){
        this.handler = handler;
        this.objectInputStream = objectInputStream;
        this.activity = activity;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                generateLastMoveContent();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateLastMoveContent() throws IOException, ClassNotFoundException {
        String success = (String) objectInputStream.readObject();
        System.out.println(success);
        String[] items = success.split(" ");
        if(items[0].equalsIgnoreCase("PROMOTE")){
            dataOutputStream.writeUTF("PROMOTE_TO QUEEN");
            //Create PopUp

        }else if(items[0].equalsIgnoreCase("PROMOTION")){
            final String[] piecePosition = items[1].split(",");
            final int piecePositionInGrid = (Integer.parseInt(piecePosition[0]) * 8) + Integer.parseInt(piecePosition[1]);
            final String receivedPieceColor = items[2];

            switch (items[3]){
                case "QUEEN":
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView view2 = (ImageView) activity.gridLayout.getChildAt(piecePositionInGrid);
                            if(receivedPieceColor.equals("WHITE")){
                                view2.setImageResource(R.drawable.chess_qlt60);
                            }else if(receivedPieceColor.equals("BLACK")){
                                view2.setImageResource(R.drawable.chess_qdt60);
                            }
                        }
                    });
                    break;
                case "KNIGHT":
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView view2 = (ImageView) activity.gridLayout.getChildAt(piecePositionInGrid);
                            if(receivedPieceColor.equals("WHITE")){
                                view2.setImageResource(R.drawable.chess_nlt60);
                            }else if(receivedPieceColor.equals("BLACK")){
                                view2.setImageResource(R.drawable.chess_ndt60);
                            }
                        }
                    });
                    break;

                case "ROOK":
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView view2 = (ImageView) activity.gridLayout.getChildAt(piecePositionInGrid);
                            if(receivedPieceColor.equals("WHITE")){
                                view2.setImageResource(R.drawable.chess_rlt60);
                            }else if(receivedPieceColor.equals("BLACK")){
                                view2.setImageResource(R.drawable.chess_rdt60);
                            }
                        }
                    });
                    break;
                case "BISHOP":
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView view2 = (ImageView) activity.gridLayout.getChildAt(piecePositionInGrid);
                            if(receivedPieceColor.equals("WHITE")){
                                view2.setImageResource(R.drawable.chess_blt60);
                            }else if(receivedPieceColor.equals("BLACK")){
                                view2.setImageResource(R.drawable.chess_bdt60);
                            }
                        }
                    });
                    break;
            }

        } else{
            final String[] firstPostion = items[1].split(",");
            final String[] secondPosition = items[2].split(",");
            System.out.println(success);
            if(items[0].equalsIgnoreCase("success")){
                //convert position data to array length data
                final int start = (Integer.parseInt(firstPostion[0]) * 8) + Integer.parseInt(firstPostion[1]);
                final int finish = (Integer.parseInt(secondPosition[0]) * 8) + Integer.parseInt(secondPosition[1]);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView lastMove = (TextView) activity.findViewById(R.id.textView67);
                        lastMove.setText(Arrays.toString(firstPostion) + " To " + Arrays.toString(secondPosition));
                        ImageView view = (ImageView) activity.gridLayout.getChildAt(start);
                        ImageView view2 = (ImageView) activity.gridLayout.getChildAt(finish);
                        view2.setImageDrawable(view.getDrawable());
                        view.setImageResource(android.R.color.transparent);
                    }
                });
            }else if (items[0].equalsIgnoreCase("failure")){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(activity, "Move Failed, {OTHERCOLOR} move again",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}


