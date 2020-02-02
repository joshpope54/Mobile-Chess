//package com.example.ce301.old;
//
//import android.app.Dialog;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.ce301.R;
//
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class readingObjectsThread extends Thread {
//    private Handler handler;
//    private Scanner objectInputStream;
//    private Game activity;
//    private PrintWriter dataOutputStream;
//    public static String[] promotionType = {""};
//
//    public readingObjectsThread(Game activity, Handler handler, Scanner objectInputStream, PrintWriter dataOutputStream){
//        this.handler = handler;
//        this.objectInputStream = objectInputStream;
//        this.activity = activity;
//        this.dataOutputStream = dataOutputStream;
//    }
//
//    @Override
//    public void run() {
//        super.run();
//        while (true){
//            generateLastMoveContent();
//        }
//    }
//
//    public void generateLastMoveContent() {
//        String success = (String) objectInputStream.nextLine();
//        System.out.println(success);
//        String[] items = success.split(" ");
//        if(items[0].equalsIgnoreCase("PROMOTE")){
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    final Dialog dialog = new Dialog(activity);
//                    dialog.setCanceledOnTouchOutside(false);
//
//                    View dialogView = LayoutInflater.from(activity).inflate(R.layout.pawn_promotion, null);
//                    dialogView.findViewById(R.id.cardview1).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dataOutputStream.println("PROMOTE_TO QUEEN");
//                            dialog.dismiss();
//                        }
//                    });
//                    dialogView.findViewById(R.id.cardview2).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //promotionType[0] = "KNIGHT";
//                            dataOutputStream.println("PROMOTE_TO KNIGHT");
//                            dialog.dismiss();
//                        }
//                    });
//                    dialogView.findViewById(R.id.cardview3).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //promotionType[0] = "ROOK";
//                            dataOutputStream.println("PROMOTE_TO ROOK");
//                            dialog.dismiss();
//                        }
//                    });
//                    dialogView.findViewById(R.id.cardview4).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //promotionType[0] = "BISHOP";
//                            dataOutputStream.println("PROMOTE_TO BISHOP");
//                            dialog.dismiss();
//                        }
//                    });
//
//                    dialog.setContentView(dialogView);
//                    dialog.show();
//
//                    Window window = dialog.getWindow();
//                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                }
//            });
//        }
//        else if(items[0].equalsIgnoreCase("PROMOTION")){
//            final String[] piecePosition = items[1].split(",");
//            final int piecePositionInGrid = (Integer.parseInt(piecePosition[0]) * 8) + Integer.parseInt(piecePosition[1]);
//            final String receivedPieceColor = items[2];
//            switch (items[3]){
//                case "QUEEN":
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ImageView view2 = (ImageView) activity.gridLayout.getChildAt(piecePositionInGrid);
//                            if(receivedPieceColor.equals("WHITE")){
//                                view2.setImageResource(R.drawable.chess_qlt60);
//                            }else if(receivedPieceColor.equals("BLACK")){
//                                view2.setImageResource(R.drawable.chess_qdt60);
//                            }
//                        }
//                    });
//                    break;
//                case "KNIGHT":
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ImageView view2 = (ImageView) activity.gridLayout.getChildAt(piecePositionInGrid);
//                            if(receivedPieceColor.equals("WHITE")){
//                                view2.setImageResource(R.drawable.chess_nlt60);
//                            }else if(receivedPieceColor.equals("BLACK")){
//                                view2.setImageResource(R.drawable.chess_ndt60);
//                            }
//                        }
//                    });
//                    break;
//
//                case "ROOK":
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ImageView view2 = (ImageView) activity.gridLayout.getChildAt(piecePositionInGrid);
//                            if(receivedPieceColor.equals("WHITE")){
//                                view2.setImageResource(R.drawable.chess_rlt60);
//                            }else if(receivedPieceColor.equals("BLACK")){
//                                view2.setImageResource(R.drawable.chess_rdt60);
//                            }
//                        }
//                    });
//                    break;
//                case "BISHOP":
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ImageView view2 = (ImageView) activity.gridLayout.getChildAt(piecePositionInGrid);
//                            if(receivedPieceColor.equals("WHITE")){
//                                view2.setImageResource(R.drawable.chess_blt60);
//                            }else if(receivedPieceColor.equals("BLACK")){
//                                view2.setImageResource(R.drawable.chess_bdt60);
//                            }
//                        }
//                    });
//                    break;
//            }
//        } else{
//            final String[] firstPostion = items[1].split(",");
//            final String[] secondPosition = items[2].split(",");
//            System.out.println(success);
//            if(items[0].equalsIgnoreCase("success")){
//                //convert position data to array length data
//                final int start = (Integer.parseInt(firstPostion[0]) * 8) + Integer.parseInt(firstPostion[1]);
//                final int finish = (Integer.parseInt(secondPosition[0]) * 8) + Integer.parseInt(secondPosition[1]);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView lastMove = (TextView) activity.findViewById(R.id.textView67);
//                        lastMove.setText(Arrays.toString(firstPostion) + " To " + Arrays.toString(secondPosition));
//                        ImageView view = (ImageView) activity.gridLayout.getChildAt(start);
//                        ImageView view2 = (ImageView) activity.gridLayout.getChildAt(finish);
//                        view2.setImageDrawable(view.getDrawable());
//                        view.setImageResource(android.R.color.transparent);
//                    }
//                });
//            }else if (items[0].equalsIgnoreCase("failure")){
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Toast.makeText(activity, "Move Failed, {OTHERCOLOR} move again",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//    }
//}
//
//
