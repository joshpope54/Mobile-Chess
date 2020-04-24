package com.example.ce301;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;
import androidx.gridlayout.widget.GridLayout;

import com.example.ce301.R;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//package com.example.ce301.old;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.gridlayout.widget.GridLayout;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.ce301.R;
//
//import java.util.Arrays;
//
public class Game extends AppCompatActivity {
    public GridLayout gridLayout;
    private int[][] points = {{-1, -1}, {-1, -1}};
    ArrayList<ArrayList<Integer>> gridConverter = new ArrayList<>();
    private ArrayList<int[]> checkedSpots;
    ServiceClass mService;
    boolean mBound = false;
    String color;
    private RelativeLayout topLayout;
    private RelativeLayout bottomLayout;
    private boolean hasKingBeenCastled = false;
    private String firstClick;
    private String secondClick;
    private ArrayList<int[]> directionsCanMoveWhilstBlockingCheck = new ArrayList<>();
    private FlexboxLayout deadPiecesLayout;
    private boolean hasKingMoved = false;
    private boolean inCheck;
    private String lastMoveMadeByPlayer;
    private ArrayList<Integer> friendlyPiecesThatCanMoveInCheck;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ServiceClass.LocalBinder binder = (ServiceClass.LocalBinder) service;
            mService = binder.getService();
            EventBus.getDefault().register(Game.this);
            mBound = true;
            Toast.makeText(Game.this, "CONNECTED",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void setBlacksGrid(int[] blackPieces, int[] whitePieces){
        RelativeLayout rookWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[0]);
        ImageView rookW = (ImageView)rookWLayout.getChildAt(0);
        ((TextView)rookWLayout.getChildAt(2)).setText("WROOK");
        rookW.setImageResource(R.drawable.chess_rlt60);

        RelativeLayout knightWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[1]);
        ImageView knightW = (ImageView) knightWLayout.getChildAt(0);
        ((TextView)knightWLayout.getChildAt(2)).setText("WKNIGHT");
        knightW.setImageResource(R.drawable.chess_nlt60);

        RelativeLayout bishopWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[2]);
        ImageView bishopW = (ImageView) bishopWLayout.getChildAt(0);
        ((TextView)bishopWLayout.getChildAt(2)).setText("WBISHOP");
        bishopW.setImageResource(R.drawable.chess_blt60);

        RelativeLayout queenWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[4]);
        ImageView queenW = (ImageView) queenWLayout.getChildAt(0);
        ((TextView)queenWLayout.getChildAt(2)).setText("WQUEEN");
        queenW.setImageResource(R.drawable.chess_qlt60);

        RelativeLayout kingWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[3]);
        ImageView kingW = (ImageView) kingWLayout.getChildAt(0);
        ((TextView)kingWLayout.getChildAt(2)).setText("WKING");
        kingW.setImageResource(R.drawable.chess_klt60);

        RelativeLayout bishop2WLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[5]);
        ImageView bishop2W = (ImageView) bishop2WLayout.getChildAt(0);
        ((TextView)bishop2WLayout.getChildAt(2)).setText("WBISHOP");
        bishop2W.setImageResource(R.drawable.chess_blt60);

        RelativeLayout knight2WLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[6]);
        ImageView knight2W = (ImageView) knight2WLayout.getChildAt(0);
        ((TextView)knight2WLayout.getChildAt(2)).setText("WKNIGHT");
        knight2W.setImageResource(R.drawable.chess_nlt60);

        RelativeLayout rook2WLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[7]);
        ImageView rook2W = (ImageView) rook2WLayout.getChildAt(0);
        ((TextView)rook2WLayout.getChildAt(2)).setText("WROOK");
        rook2W.setImageResource(R.drawable.chess_rlt60);

        RelativeLayout rookBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[0]);
        ImageView rookB = (ImageView) rookBLayout.getChildAt(0);
        ((TextView)rookBLayout.getChildAt(2)).setText("BROOK");
        rookB.setImageResource(R.drawable.chess_rdt60);

        RelativeLayout knightBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[1]);
        ImageView knightB = (ImageView) knightBLayout.getChildAt(0);
        ((TextView)knightBLayout.getChildAt(2)).setText("BKNIGHT");
        knightB.setImageResource(R.drawable.chess_ndt60);

        RelativeLayout bishopBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[2]);
        ImageView bishopB = (ImageView) bishopBLayout.getChildAt(0);
        ((TextView)bishopBLayout.getChildAt(2)).setText("BBISHOP");
        bishopB.setImageResource(R.drawable.chess_bdt60);

        RelativeLayout queenBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[4]);
        ImageView queenB = (ImageView) queenBLayout.getChildAt(0);
        ((TextView)queenBLayout.getChildAt(2)).setText("BQUEEN");
        queenB.setImageResource(R.drawable.chess_qdt60);

        RelativeLayout kingBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[3]);
        ImageView kingB = (ImageView) kingBLayout.getChildAt(0);
        ((TextView)kingBLayout.getChildAt(2)).setText("BKING");
        kingB.setImageResource(R.drawable.chess_kdt60);

        RelativeLayout bishop2BLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[5]);
        ImageView bishop2B = (ImageView) bishop2BLayout.getChildAt(0);
        ((TextView)bishop2BLayout.getChildAt(2)).setText("BBISHOP");
        bishop2B.setImageResource(R.drawable.chess_bdt60);

        RelativeLayout knight2BLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[6]);
        ImageView knight2B = (ImageView) knight2BLayout.getChildAt(0);
        ((TextView)knight2BLayout.getChildAt(2)).setText("BKNIGHT");
        knight2B.setImageResource(R.drawable.chess_ndt60);

        RelativeLayout rook2BLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[7]);
        ImageView rook2B = (ImageView) rook2BLayout.getChildAt(0);
        ((TextView)rook2BLayout.getChildAt(2)).setText("BROOK");
        rook2B.setImageResource(R.drawable.chess_rdt60);
    }

    public void setWhitesGrid(int[] whitePieces, int[] blackPieces){
        RelativeLayout rookWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[0]);
        setRookOnClick(rookWLayout);
        ImageView rookW = (ImageView)rookWLayout.getChildAt(0);
        ((TextView)rookWLayout.getChildAt(2)).setText("WROOK");
        rookW.setImageResource(R.drawable.chess_rlt60);

        RelativeLayout knightWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[1]);
        setKnightOnClick(knightWLayout);
        ImageView knightW = (ImageView) knightWLayout.getChildAt(0);
        ((TextView)knightWLayout.getChildAt(2)).setText("WKNIGHT");
        knightW.setImageResource(R.drawable.chess_nlt60);

        RelativeLayout bishopWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[2]);
        setBishopOnClick(bishopWLayout);
        ImageView bishopW = (ImageView) bishopWLayout.getChildAt(0);
        ((TextView)bishopWLayout.getChildAt(2)).setText("WBISHOP");
        bishopW.setImageResource(R.drawable.chess_blt60);

        RelativeLayout queenWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[3]);
        setQueenOnClick(queenWLayout);
        ImageView queenW = (ImageView) queenWLayout.getChildAt(0);
        ((TextView)queenWLayout.getChildAt(2)).setText("WQUEEN");
        queenW.setImageResource(R.drawable.chess_qlt60);

        RelativeLayout kingWLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[4]);
        setKingOnClick(kingWLayout);
        ImageView kingW = (ImageView) kingWLayout.getChildAt(0);
        ((TextView)kingWLayout.getChildAt(2)).setText("WKING");
        kingW.setImageResource(R.drawable.chess_klt60);

        RelativeLayout bishop2WLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[5]);
        setBishopOnClick(bishop2WLayout);
        ImageView bishop2W = (ImageView) bishop2WLayout.getChildAt(0);
        ((TextView)bishop2WLayout.getChildAt(2)).setText("WBISHOP");
        bishop2W.setImageResource(R.drawable.chess_blt60);

        RelativeLayout knight2WLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[6]);
        setKnightOnClick(knight2WLayout);
        ImageView knight2W = (ImageView) knight2WLayout.getChildAt(0);
        ((TextView)knight2WLayout.getChildAt(2)).setText("WKNIGHT");
        knight2W.setImageResource(R.drawable.chess_nlt60);

        RelativeLayout rook2WLayout = (RelativeLayout) gridLayout.getChildAt(whitePieces[7]);
        setRookOnClick(rook2WLayout);
        ImageView rook2W = (ImageView) rook2WLayout.getChildAt(0);
        ((TextView)rook2WLayout.getChildAt(2)).setText("WROOK");
        rook2W.setImageResource(R.drawable.chess_rlt60);

        RelativeLayout rookBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[0]);
        ImageView rookB = (ImageView) rookBLayout.getChildAt(0);
        ((TextView)rookBLayout.getChildAt(2)).setText("BROOK");
        rookB.setImageResource(R.drawable.chess_rdt60);

        RelativeLayout knightBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[1]);
        ImageView knightB = (ImageView) knightBLayout.getChildAt(0);
        ((TextView)knightBLayout.getChildAt(2)).setText("BKNIGHT");
        knightB.setImageResource(R.drawable.chess_ndt60);

        RelativeLayout bishopBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[2]);
        ImageView bishopB = (ImageView) bishopBLayout.getChildAt(0);
        ((TextView)bishopBLayout.getChildAt(2)).setText("BBISHOP");
        bishopB.setImageResource(R.drawable.chess_bdt60);

        RelativeLayout queenBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[3]);
        ImageView queenB = (ImageView) queenBLayout.getChildAt(0);
        ((TextView)queenBLayout.getChildAt(2)).setText("BQUEEN");
        queenB.setImageResource(R.drawable.chess_qdt60);

        RelativeLayout kingBLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[4]);
        ImageView kingB = (ImageView) kingBLayout.getChildAt(0);
        ((TextView)kingBLayout.getChildAt(2)).setText("BKING");
        kingB.setImageResource(R.drawable.chess_kdt60);

        RelativeLayout bishop2BLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[5]);
        ImageView bishop2B = (ImageView) bishop2BLayout.getChildAt(0);
        ((TextView)bishop2BLayout.getChildAt(2)).setText("BBISHOP");
        bishop2B.setImageResource(R.drawable.chess_bdt60);

        RelativeLayout knight2BLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[6]);
        ImageView knight2B = (ImageView) knight2BLayout.getChildAt(0);
        ((TextView)knight2BLayout.getChildAt(2)).setText("BKNIGHT");
        knight2B.setImageResource(R.drawable.chess_ndt60);

        RelativeLayout rook2BLayout = (RelativeLayout) gridLayout.getChildAt(blackPieces[7]);
        ImageView rook2B = (ImageView) rook2BLayout.getChildAt(0);
        ((TextView)rook2BLayout.getChildAt(2)).setText("BROOK");
        rook2B.setImageResource(R.drawable.chess_rdt60);
    }

    public void clearSpots(){

        for(int j = 0; j<gridLayout.getChildCount(); j++){
            RelativeLayout relativeLayout = (RelativeLayout)gridLayout.getChildAt(j);
            int finalPosition = findPosition(relativeLayout);
            for(int i = 0; i<gridConverter.size(); i++) {
                if (gridConverter.get(i).contains(finalPosition)) {
                    int gridXPosition = i;
                    int gridYPosition = gridConverter.get(i).indexOf(finalPosition);
                    TextView text = (TextView)relativeLayout.getChildAt(2);
                    if(text.getText().equals("SPOT")){
                        text.setText("");
                        ImageView image = (ImageView)relativeLayout.getChildAt(0);
                        image.setImageResource(R.drawable.chess_trans60);
                    }else if (text.getText().toString().contains("SPOT ")){
                        text.setText(text.getText().toString().substring(text.getText().toString().lastIndexOf(" ")));
                        if(gridXPosition%2==0){
                            //even spots are dark
                            if(gridYPosition%2==0){
                                relativeLayout.setBackgroundColor(getResources().getColor(R.color.lightBoard));
                            }else{
                                relativeLayout.setBackgroundColor(getResources().getColor(R.color.darkBoard));
                            }
                        }else {
                            //odd spots are dark
                            if(gridYPosition%2==0){
                                relativeLayout.setBackgroundColor(getResources().getColor(R.color.darkBoard));
                            }else{
                                relativeLayout.setBackgroundColor(getResources().getColor(R.color.lightBoard));
                            }
                        }
                    }
                }
            }

        }
    }

    public void setSpotOnClick(RelativeLayout layout){
        final int finalPosition = findPosition(layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<gridConverter.size(); i++){
                    if(gridConverter.get(i).contains(finalPosition)) {
                        //i = vertical array position
                        int gridXPosition = i;
                        int gridYPosition = gridConverter.get(i).indexOf(finalPosition);
                        if (points[1][0]==-1){
                            secondClick = "SPOT";
                            if(!hasKingMoved && firstClick.contains("KING")){
                                hasKingMoved = true;
                            }
                            firstClick = "";
                            secondClick = "";
                            points[1][0]= gridXPosition;
                            points[1][1]= gridYPosition;
                            checkData();
                        }
                    }
                }
                clearSpots();
            }
        });

    }

    public int findPosition(RelativeLayout layout){
        int position = 0; //item number with the array (ie 45)
        for(int i = 0; i<gridLayout.getChildCount(); i++){
            if(layout.equals((RelativeLayout)gridLayout.getChildAt(i))){
                break;
            }else {
                position++;
            }
        }
        return position;
    }

    public void resetGrid(){
        points[0][0] = -1;
        points[0][1] = -1;
        points[1][0] = -1;
        points[1][1] = -1;
    }

    public ArrayList<int[]> getPointsThatCanMoveToWhilstBlockingCheck(RelativeLayout layout){
        int kingSelectionNumber = findKingByName();
        int kingX = -1;
        int kingY = -1;
        ArrayList<int[]> positionsCanMoveTo = new ArrayList<>();
        for (int i = 0; i < gridConverter.size(); i++) {
            if (gridConverter.get(i).contains(kingSelectionNumber)) {
                int gridXPosition = i;
                int gridYPosition = gridConverter.get(i).indexOf(kingSelectionNumber);
                kingX = gridXPosition;
                kingY = gridYPosition;
            }
        }
        directionsCanMoveWhilstBlockingCheck = new ArrayList<>();
        if(blockingCheck(layout, kingX, kingY)){
            for (int[] position : directionsCanMoveWhilstBlockingCheck) {
                for (int j = kingX + position[0], k = kingY + position[1]; ((k >= 0 && k <= 7) && (j >= 0 && j <= 7)); j += position[0], k += position[1]) {
                    int gridLayoutPosition = gridConverter.get(j).get(k);
                    RelativeLayout pieceCanMoveTo = (RelativeLayout) gridLayout.getChildAt(gridLayoutPosition);
                    TextView view = (TextView) pieceCanMoveTo.getChildAt(2);
                    if (view.getText().toString().equals("")) {
                        int[] array = new int[2];
                        array[0] = j;
                        array[1] = k;
                        positionsCanMoveTo.add(array);
                    }else if (view.getText().toString().charAt(0) != color.charAt(0)){
                        int[] array = new int[2];
                        array[0] = j;
                        array[1] = k;
                        positionsCanMoveTo.add(array);
                        break;
                    }else if (view.getText().toString().charAt(0) == color.charAt(0)){
                        continue;
                    }
                }
            }
        }
        return positionsCanMoveTo;
    }

    public boolean blockingCheck(RelativeLayout inputLayout, int kingX, int kingY){
        if(kingX!=-1 && kingY!=-1) {
            int[][] potentialPositions = {{+1, 0}, {+1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, +1}, {0, +1}, {+1, +1}};
            outer: for (int[] position : potentialPositions) {
                boolean pieceFound = false;
                inner: for (int j = kingX + position[0], k = kingY + position[1]; ((k >= 0 && k <= 7) && (j >= 0 && j <= 7)); j += position[0], k += position[1]) {
                    int gridLayoutPosition = gridConverter.get(j).get(k);
                    RelativeLayout layout = (RelativeLayout) gridLayout.getChildAt(gridLayoutPosition);
                    TextView loopingTextView = (TextView)layout.getChildAt(2);
                    if(!loopingTextView.getText().equals("")) {
                        if (loopingTextView.getText().toString().charAt(0) == color.charAt(0)) {
                            if(layout.equals(inputLayout)){
                                pieceFound = true;
                            }else{
                                continue outer;
                            }
                        }
                    }
                    if(pieceFound){
                        if(!loopingTextView.getText().equals("")) {
                            if (loopingTextView.getText().toString().charAt(0) != color.charAt(0)) {
                                if(loopingTextView.getText().toString().contains("QUEEN") || loopingTextView.getText().toString().contains("BISHOP") || loopingTextView.getText().toString().contains("ROOK")){
                                    directionsCanMoveWhilstBlockingCheck.add(position);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setPawnOnClick(RelativeLayout layout){
        final int finalPosition = findPosition(layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstClick.equals("PAWN"+finalPosition)){
                    //this piece
                    clearSpots();
                    resetGrid();
                    firstClick = "";
                    secondClick = "";
                }else{
                    clearSpots();
                    resetGrid();
                    firstClick = "PAWN"+finalPosition;
                    secondClick = "";
                    for (int i = 0; i < gridConverter.size(); i++) {
                        if (gridConverter.get(i).contains(finalPosition)) {
                            //i = vertical array position
                            int gridXPosition = i;
                            int gridYPosition = gridConverter.get(i).indexOf(finalPosition);
                            if (points[0][0] == -1) {
                                points[0][0] = gridXPosition;
                                points[0][1] = gridYPosition;
                            }
                            if (inCheck) {
                                for(int[] position: checkedSpots){
                                    System.out.println(Arrays.toString(position));

                                    if(canThisPawnMove(position[0], position[1], gridXPosition, gridYPosition)){
                                        RelativeLayout theLayoutOneForward = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(position[0]).get(position[1]));
                                        TextView textOneForward = (TextView) theLayoutOneForward.getChildAt(2);
                                        if (textOneForward.getText().equals("")) {
                                            textOneForward.setText("SPOT");
                                            setSpotOnClick(theLayoutOneForward);
                                            ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(position[0]).get(position[1]))).getChildAt(0);
                                            image.setImageResource(R.drawable.chess_gotospot);
                                        }else{
                                            //only perform if its diagonal
                                            if(position[0]-gridXPosition == -1 && (position[1]-gridYPosition==+1 || position[1]-gridYPosition==-1)){
                                                if (textOneForward.getText().length() != 0) {
                                                    if (textOneForward.getText().charAt(0) != color.charAt(0)) {
                                                        theLayoutOneForward.setBackgroundColor(getResources().getColor(R.color.green));
                                                        textOneForward.setText("SPOT " + textOneForward.getText().toString());
                                                        setSpotOnClick(theLayoutOneForward);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }else{
                                if (gridXPosition == 6) {
                                    //two forward
                                    RelativeLayout theLayoutOneForward = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition-1).get(gridYPosition));
                                    TextView textOneForward = (TextView) theLayoutOneForward.getChildAt(2);
                                    RelativeLayout theLayoutTwoForward = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition-2).get(gridYPosition));
                                    TextView textTwoForward = (TextView) theLayoutTwoForward.getChildAt(2);
                                    if (textOneForward.getText().equals("")) {
                                        textOneForward.setText("SPOT");
                                        setSpotOnClick(theLayoutOneForward);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition-1).get(gridYPosition))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                        if (textTwoForward.getText().equals("")) {
                                            textTwoForward.setText("SPOT");
                                            setSpotOnClick(theLayoutTwoForward);
                                            ImageView image2 = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition-2).get(gridYPosition))).getChildAt(0);
                                            image2.setImageResource(R.drawable.chess_gotospot);
                                        }
                                    }

                                    if (gridYPosition + 1 <= 7) {
                                        RelativeLayout upAndRightLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition - 1).get(gridYPosition + 1));
                                        TextView textDiagUpAndRight = (TextView) upAndRightLayout.getChildAt(2);
                                        if (textDiagUpAndRight.getText().length() != 0) {
                                            if (textDiagUpAndRight.getText().charAt(0) != color.charAt(0)) {
                                                upAndRightLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                textDiagUpAndRight.setText("SPOT " + textDiagUpAndRight.getText().toString());
                                                setSpotOnClick(upAndRightLayout);
                                            }
                                        }
                                    }
                                    if (gridYPosition - 1 >= 0) {
                                        RelativeLayout upAndLeftLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition - 1).get(gridYPosition - 1));
                                        TextView textDiagUpAndLeft = (TextView) upAndLeftLayout.getChildAt(2);
                                        if (textDiagUpAndLeft.getText().length() != 0) {
                                            if (textDiagUpAndLeft.getText().charAt(0) != color.charAt(0)) {
                                                upAndLeftLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                textDiagUpAndLeft.setText("SPOT " + textDiagUpAndLeft.getText().toString());
                                                setSpotOnClick(upAndLeftLayout);
                                            }
                                        }
                                    }


                                } else {

                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition - 1).get(gridYPosition));
                                    TextView text = (TextView) theLayout.getChildAt(2);

                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition - 1).get(gridYPosition))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    }

                                    if (gridYPosition + 1 <= 7) {
                                        RelativeLayout upAndRightLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition - 1).get(gridYPosition + 1));
                                        TextView textDiagUpAndRight = (TextView) upAndRightLayout.getChildAt(2);
                                        if (textDiagUpAndRight.getText().length() != 0) {
                                            if (textDiagUpAndRight.getText().charAt(0) != color.charAt(0)) {
                                                upAndRightLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                textDiagUpAndRight.setText("SPOT " + textDiagUpAndRight.getText().toString());
                                                setSpotOnClick(upAndRightLayout);
                                            }
                                        }
                                    }
                                    if (gridYPosition - 1 >= 0) {
                                        RelativeLayout upAndLeftLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition - 1).get(gridYPosition - 1));
                                        TextView textDiagUpAndLeft = (TextView) upAndLeftLayout.getChildAt(2);

                                        if (textDiagUpAndLeft.getText().length() != 0) {
                                            if (textDiagUpAndLeft.getText().charAt(0) != color.charAt(0)) {
                                                upAndLeftLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                textDiagUpAndLeft.setText("SPOT " + textDiagUpAndLeft.getText().toString());
                                                setSpotOnClick(upAndLeftLayout);
                                            }
                                        }
                                    }

                                    if(gridXPosition==3){ // on correct row
                                        //check that the last move is a pawn etc etc blah blah blah
                                        String[] moves = lastMoveMadeByPlayer.split(" ");
                                        System.out.println(Arrays.toString(moves));
                                        int finishXPosition = 0;
                                        int finishYPosition = 0;
                                        //X Y of end position
                                        for (int x = 0; x < gridConverter.size(); x++) {
                                            if (gridConverter.get(x).contains(Integer.parseInt(moves[1]))) {
                                                finishXPosition = x;
                                                finishYPosition = gridConverter.get(x).indexOf(Integer.parseInt(moves[1]));
                                            }
                                        }
                                        RelativeLayout lastMoveLayoutItem = (RelativeLayout) gridLayout.getChildAt(Integer.parseInt(moves[1]));
                                        TextView lastMoveLayoutText = (TextView) lastMoveLayoutItem.getChildAt(2);
                                        if(lastMoveLayoutText.getText().toString().contains("PAWN")){
                                            if(finishXPosition==3){
                                                if(finishYPosition == gridYPosition + 1){
                                                    //create spot
                                                    RelativeLayout enpassantSpot = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(finishXPosition - 1).get(finishYPosition));
                                                    TextView enpassantSpotText = (TextView) enpassantSpot.getChildAt(2);
                                                    if (enpassantSpotText.getText().equals("")) {
                                                        enpassantSpotText.setText("SPOT");
                                                        setSpotOnClick(enpassantSpot);
                                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(finishXPosition - 1).get(finishYPosition))).getChildAt(0);
                                                        image.setImageResource(R.drawable.chess_gotospot);
                                                    }
                                                }else if (finishYPosition == gridYPosition - 1){
                                                    //create spot
                                                    RelativeLayout enpassantSpot = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(finishXPosition - 1).get(finishYPosition));
                                                    TextView enpassantSpotText = (TextView) enpassantSpot.getChildAt(2);
                                                    if (enpassantSpotText.getText().equals("")) {
                                                        enpassantSpotText.setText("SPOT");
                                                        setSpotOnClick(enpassantSpot);
                                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(finishXPosition - 1).get(finishYPosition))).getChildAt(0);
                                                        image.setImageResource(R.drawable.chess_gotospot);
                                                    }
                                                }
                                            }
                                        }



                                    }

                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void setBishopOnClick(final RelativeLayout layout){
        final int finalPosition = findPosition(layout);
        resetGrid();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(firstClick.equals("BISHOP"+finalPosition)){
                    //this piece
                    clearSpots();
                    resetGrid();
                    firstClick = "";
                    secondClick = "";
                }else {
                    clearSpots();
                    resetGrid();
                    firstClick = "BISHOP" + finalPosition;
                    secondClick = "";
                    for (int i = 0; i < gridConverter.size(); i++) {
                        if (gridConverter.get(i).contains(finalPosition)) {
                            int gridXPosition = i;
                            int gridYPosition = gridConverter.get(i).indexOf(finalPosition);
                            if (points[0][0] == -1) {
                                points[0][0] = gridXPosition;
                                points[0][1] = gridYPosition;
                            }
                            //System.out.println("Up and left");

                            int stoppingSpotXUpLeft = 0;
                            int stoppingSpotYUpLeft = 0;

                            int stoppingSpotXDownLeft = 7;
                            int stoppingSpotYDownLeft = 0;

                            int stoppingSpotXDownRight = 7;
                            int stoppingSpotYDownRight = 7;

                            int stoppingSpotXUpRight = 0;
                            int stoppingSpotYUpRight = 7;
//                            ArrayList<int[]> array = blockingCheck(layout);
//                            if(array.size()!=0){
//                                for(int[] position: array){
//                                    System.out.println("FROM BISHOP POSITION   "  + Arrays.toString(position));
//                                    if(canBishopMoveToSpot(position[0], position[1], "BISHOP","FRIENDLY")){
//                                        System.out.println("BISHOP CAN MOVE TO THIS PIECE ^");
//                                        int[][] directions = {{+1, -1}, {-1, -1}, {-1, +1}, {+1, +1}};
//                                        for (int[] direction : directions) {
//
//                                            //find which direction to use?
//
//
//                                            for (int j = gridXPosition + direction[0], k = gridYPosition + direction[1]; ((k==position[1]) && (j==position[0])); j += direction[0], k += direction[1]) {
//                                                System.out.println("MOVING  " + " J:" + j + " K:" + k);
//                                            }
//                                        }
//                                    }
//                                }
//                            }else{
                            ArrayList<int[]> arrayOfPoints = getPointsThatCanMoveToWhilstBlockingCheck(layout);
                            if (arrayOfPoints.size()!=0) {
                                Collections.reverse(arrayOfPoints);
                                for (int[] array : arrayOfPoints) {
                                    boolean canMove = canThisBishopMoveToSpot(array[0], array[1], "FRIENDLY", finalPosition);
                                    if(canMove){
                                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(array[0]).get(array[1]));
                                        TextView textView = (TextView) theLayout.getChildAt(2);
                                        System.out.println(textView.getText());
                                        if (textView.getText().equals("")){
                                            textView.setText("SPOT");
                                            setSpotOnClick(theLayout);
                                            ImageView image = (ImageView) theLayout.getChildAt(0);
                                            image.setImageResource(R.drawable.chess_gotospot);
                                        } else if (textView.getText().charAt(0) != color.charAt(0)) {
                                            theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                            textView.setText("SPOT " + textView.getText());
                                            setSpotOnClick(theLayout);
                                        }
                                    }
                                }
                                //direction bishop can move {-1,-1} {+1,+1} {-1,+1} {+1,-1}
                            } else {
                                if (inCheck) {
                                    for (int[] position : checkedSpots) {
                                        if (canBishopMoveToSpot(position[0], position[1], "BISHOP", "FRIENDLY")) {
                                            stoppingSpotXUpLeft = position[0];
                                            stoppingSpotYUpLeft = position[1];
                                            stoppingSpotXUpRight = position[0];
                                            stoppingSpotYUpRight = position[1];
                                            stoppingSpotXDownLeft = position[0];
                                            stoppingSpotYDownLeft = position[1];
                                            stoppingSpotXDownRight = position[0];
                                            stoppingSpotYDownRight = position[1];
                                        }
                                    }
                                }
                                for (int k = gridYPosition - 1, j = gridXPosition - 1; (k >= stoppingSpotYUpLeft && j >= stoppingSpotXUpLeft); k--, j--) {
                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                    TextView text = (TextView) theLayout.getChildAt(2);
                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    } else if (text.getText().charAt(0) != color.charAt(0)) {
                                        theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                        text.setText("SPOT " + text.getText());
                                        setSpotOnClick(theLayout);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                                //System.out.println("down and left");
                                for (int k = gridYPosition - 1, j = gridXPosition + 1; (k >= stoppingSpotYDownLeft && j <= stoppingSpotXDownLeft); k--, j++) {
                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                    TextView text = (TextView) theLayout.getChildAt(2);
                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    } else if (text.getText().charAt(0) != color.charAt(0)) {
                                        theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                        text.setText("SPOT " + text.getText());
                                        setSpotOnClick(theLayout);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                                //System.out.println("down and right");
                                for (int k = gridYPosition + 1, j = gridXPosition + 1; (k <= stoppingSpotYDownRight && j <= stoppingSpotXDownRight); k++, j++) {
                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                    TextView text = (TextView) theLayout.getChildAt(2);
                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    } else if (text.getText().charAt(0) != color.charAt(0)) {
                                        theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                        text.setText("SPOT " + text.getText());
                                        setSpotOnClick(theLayout);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                                //System.out.println("up and right");
                                for (int k = gridYPosition + 1, j = gridXPosition - 1; (k <= stoppingSpotYUpRight && j >= stoppingSpotXUpRight); k++, j--) {
                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                    TextView text = (TextView) theLayout.getChildAt(2);
                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    } else if (text.getText().charAt(0) != color.charAt(0)) {
                                        theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                        text.setText("SPOT " + text.getText());
                                        setSpotOnClick(theLayout);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                        }
                    }
                }

        });
    }

    public void setKnightOnClick(final RelativeLayout layout){
        final int finalPosition = findPosition(layout);
        resetGrid();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstClick.equals("KNIGHT"+finalPosition)){
                    //this piece
                    clearSpots();
                    resetGrid();
                    firstClick = "";
                    secondClick = "";
                }else {
                    clearSpots();
                    resetGrid();
                    firstClick = "KNIGHT" + finalPosition;
                    secondClick = "";
                    int[][] potentialPositions = {{2, 1}, {1, 2}, {-2, 1}, {-1, 2}, {-2, -1}, {-1, -2}, {2, -1}, {1, -2}};
                    for (int i = 0; i < gridConverter.size(); i++) {
                        if (gridConverter.get(i).contains(finalPosition)) {
                            int gridXPosition = i;
                            int gridYPosition = gridConverter.get(i).indexOf(finalPosition);
                            if (points[0][0] == -1) {
                                points[0][0] = gridXPosition;
                                points[0][1] = gridYPosition;
                            }
                            ArrayList<int[]> arrayOfPoints = getPointsThatCanMoveToWhilstBlockingCheck(layout);
                            if (arrayOfPoints.size()!=0) {
                                Collections.reverse(arrayOfPoints);
                                for (int[] array : arrayOfPoints) {
                                    boolean canMove = canThisKnightMoveToSpot(array[0], array[1], "FRIENDLY", finalPosition);
                                    if(canMove){
                                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(array[0]).get(array[1]));
                                        TextView textView = (TextView) theLayout.getChildAt(2);
                                        System.out.println(textView.getText());
                                        if (textView.getText().equals("")){
                                            textView.setText("SPOT");
                                            setSpotOnClick(theLayout);
                                            ImageView image = (ImageView) theLayout.getChildAt(0);
                                            image.setImageResource(R.drawable.chess_gotospot);
                                        } else if (textView.getText().charAt(0) != color.charAt(0)) {
                                            theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                            textView.setText("SPOT " + textView.getText());
                                            setSpotOnClick(theLayout);
                                        }
                                    }
                                }
                                //direction bishop can move {-1,-1} {+1,+1} {-1,+1} {+1,-1}
                            } else {
                                if (inCheck) {
                                    for (int[] position : checkedSpots) {
                                        if (canKnightMoveToSpot(position[0], position[1], "FRIENDLY")) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(position[0]).get(position[1]));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(position[0]).get(position[1]))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                            }
                                        }
                                    }
                                } else {
                                    for (int[] possibleMove : potentialPositions) {
                                        if ((gridXPosition + possibleMove[1] >= 0 && gridXPosition + possibleMove[1] <= 7) && (gridYPosition + possibleMove[0] >= 0 && gridYPosition + possibleMove[0] <= 7)) {
                                            int j = gridXPosition + possibleMove[1];
                                            int k = gridYPosition + possibleMove[0];
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void runRookCutOff(int gridXPosition, int gridYPosition, String typeOfPiece){
        for(int[] position: checkedSpots){
            if(canRookMoveToSpot(position[0], position[1], typeOfPiece,"FRIENDLY")){
                if(position[0]>gridXPosition && position[1]==gridYPosition){  //down
                    for (int j = gridXPosition+1; j <= position[1]; j++) {
                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridYPosition));
                        TextView text = (TextView) theLayout.getChildAt(2);
                        if (text.getText().equals("")) {
                            text.setText("SPOT");
                            setSpotOnClick(theLayout);
                            ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridYPosition))).getChildAt(0);
                            image.setImageResource(R.drawable.chess_gotospot);
                            //set spots onclick
                        } else if (text.getText().charAt(0) != color.charAt(0)) {
                            theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                            text.setText("SPOT " + text.getText());
                            setSpotOnClick(theLayout);
                            break;
                        } else {
                            break;
                        }
                    }
                }else if(position[0]<gridXPosition && position[1]==gridYPosition){//up
                    for (int j = gridXPosition-1; j >= position[0]; j--) {
                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridYPosition));
                        TextView text = (TextView) theLayout.getChildAt(2);
                        if (text.getText().equals("")) {
                            text.setText("SPOT");
                            setSpotOnClick(theLayout);
                            ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridYPosition))).getChildAt(0);
                            image.setImageResource(R.drawable.chess_gotospot);
                            //set spots onclick
                        } else if (text.getText().charAt(0) != color.charAt(0)) {
                            theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                            text.setText("SPOT " + text.getText());
                            setSpotOnClick(theLayout);
                            break;
                        } else {
                            break;
                        }

                    }
                }else if(position[0]==gridXPosition && position[1]>gridYPosition){//right
                    for (int j = gridYPosition+1; j <= position[1]; j++) {
                        System.out.println(gridXPosition + " " + j);
                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j));
                        TextView text = (TextView) theLayout.getChildAt(2);
                        if (text.getText().equals("")) {
                            text.setText("SPOT");
                            setSpotOnClick(theLayout);
                            ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j))).getChildAt(0);
                            image.setImageResource(R.drawable.chess_gotospot);
                            //set spots onclick
                        } else if (text.getText().charAt(0) != color.charAt(0)) {
                            theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                            text.setText("SPOT " + text.getText());
                            setSpotOnClick(theLayout);
                            break;
                        } else {
                            break;
                        }
                    }
                }else if(position[0]>gridXPosition && position[1]<gridYPosition){//left
                    for (int j = gridYPosition-1; j >= position[1]; j--) {
                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j));
                        TextView text = (TextView) theLayout.getChildAt(2);
                        if (text.getText().equals("")) {
                            text.setText("SPOT");
                            setSpotOnClick(theLayout);
                            ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j))).getChildAt(0);
                            image.setImageResource(R.drawable.chess_gotospot);
                            //set spots onclick
                        } else if (text.getText().charAt(0) != color.charAt(0)) {
                            theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                            text.setText("SPOT " + text.getText());
                            setSpotOnClick(theLayout);
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setRookOnClick(final RelativeLayout layout){
        final int finalPosition = findPosition(layout);
        resetGrid();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstClick.equals("ROOK"+finalPosition)){
                    //this piece
                    clearSpots();
                    resetGrid();
                    firstClick = "";
                    secondClick = "";
                }else {
                    clearSpots();
                    resetGrid();
                    firstClick = "ROOK" + finalPosition;
                    secondClick = "";
                    for (int i = 0; i < gridConverter.size(); i++) {
                        if (gridConverter.get(i).contains(finalPosition)) {
                            //i = vertical direction
                            int gridXPosition = i;
                            int gridYPosition = gridConverter.get(i).indexOf(finalPosition);
                            if (points[0][0] == -1) {
                                points[0][0] = gridXPosition;
                                points[0][1] = gridYPosition;
                            }

                            ArrayList<int[]> arrayOfPoints = getPointsThatCanMoveToWhilstBlockingCheck(layout);
                            if (arrayOfPoints.size()!=0) {
                                Collections.reverse(arrayOfPoints);
                                for (int[] array : arrayOfPoints) {
                                    boolean canMove = canThisRookMoveToSpot(array[0], array[1], "FRIENDLY", finalPosition);
                                    if(canMove){
                                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(array[0]).get(array[1]));
                                        TextView textView = (TextView) theLayout.getChildAt(2);
                                        System.out.println(textView.getText());
                                        if (textView.getText().equals("")){
                                            textView.setText("SPOT");
                                            setSpotOnClick(theLayout);
                                            ImageView image = (ImageView) theLayout.getChildAt(0);
                                            image.setImageResource(R.drawable.chess_gotospot);
                                        } else if (textView.getText().charAt(0) != color.charAt(0)) {
                                            theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                            textView.setText("SPOT " + textView.getText());
                                            setSpotOnClick(theLayout);
                                        }
                                    }
                                }
                                //direction bishop can move {-1,-1} {+1,+1} {-1,+1} {+1,-1}
                            } else {
                                if (inCheck) {
                                    runRookCutOff(gridXPosition, gridYPosition, "ROOK");
                                } else {
                                    if (gridYPosition >= 0) {
                                        for (int j = gridYPosition - 1; j >= 0; j--) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                                //set spots onclick
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    if (gridYPosition <= 7) {
                                        //loop right
                                        for (int j = gridYPosition + 1; j <= 7; j++) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    //loop up
                                    if (gridXPosition - 1 >= 0) {
                                        for (int j = gridXPosition - 1; j >= 0; j--) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridYPosition));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridYPosition))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    if (gridXPosition + 1 <= 7) {
                                        for (int j = gridXPosition + 1; j <= 7; j++) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridYPosition));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridYPosition))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void setQueenOnClick(final RelativeLayout layout){
        final int finalPosition = findPosition(layout);
        resetGrid();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstClick.equals("QUEEN" + finalPosition)) {
                    //this piece
                    clearSpots();
                    resetGrid();
                    firstClick = "";
                    secondClick = "";
                } else {
                    clearSpots();
                    resetGrid();
                    firstClick = "QUEEN" + finalPosition;
                    secondClick = "";

                    for (int i = 0; i < gridConverter.size(); i++) {
                        if (gridConverter.get(i).contains(finalPosition)) {
                            int gridXPosition = i;
                            int gridYPosition = gridConverter.get(i).indexOf(finalPosition);
                            if (points[0][0] == -1) {
                                points[0][0] = gridXPosition;
                                points[0][1] = gridYPosition;
                            }
                            //System.out.println("Up and left");
                            int stoppingSpotXUpLeft = 0;
                            int stoppingSpotYUpLeft = 0;
                            int stoppingSpotXDownLeft = 7;
                            int stoppingSpotYDownLeft = 0;
                            int stoppingSpotXDownRight = 7;
                            int stoppingSpotYDownRight = 7;
                            int stoppingSpotXUpRight = 0;
                            int stoppingSpotYUpRight = 7;
                            ArrayList<int[]> arrayOfPoints = getPointsThatCanMoveToWhilstBlockingCheck(layout);
                            if (arrayOfPoints.size()!=0) {
                                Collections.reverse(arrayOfPoints);
                                for (int[] array : arrayOfPoints) {
                                    boolean canMove = canThisQueenMoveToSpot(array[0], array[1], "FRIENDLY", finalPosition);
                                    if(canMove){
                                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(array[0]).get(array[1]));
                                        TextView textView = (TextView) theLayout.getChildAt(2);
                                        System.out.println(textView.getText());
                                        if (textView.getText().equals("")){
                                            textView.setText("SPOT");
                                            setSpotOnClick(theLayout);
                                            ImageView image = (ImageView) theLayout.getChildAt(0);
                                            image.setImageResource(R.drawable.chess_gotospot);
                                        } else if (textView.getText().charAt(0) != color.charAt(0)) {
                                            theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                            textView.setText("SPOT " + textView.getText());
                                            setSpotOnClick(theLayout);
                                        }
                                    }
                                }
                                //direction bishop can move {-1,-1} {+1,+1} {-1,+1} {+1,-1}
                            } else {
                                if (inCheck) {
                                    runRookCutOff(gridXPosition, gridYPosition, "QUEEN");
                                    for (int[] position : checkedSpots) {
                                        if (canQueenMoveToSpot(position[0], position[1], "FRIENDLY")) {
                                            stoppingSpotXUpLeft = position[0];
                                            stoppingSpotYUpLeft = position[1];
                                            stoppingSpotXUpRight = position[0];
                                            stoppingSpotYUpRight = position[1];
                                            stoppingSpotXDownLeft = position[0];
                                            stoppingSpotYDownLeft = position[1];
                                            stoppingSpotXDownRight = position[0];
                                            stoppingSpotYDownRight = position[1];
                                        }
                                    }
                                }
                                for (int k = gridYPosition - 1, j = gridXPosition - 1; (k >= stoppingSpotYUpLeft && j >= stoppingSpotXUpLeft); k--, j--) { //up and left
                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                    TextView text = (TextView) theLayout.getChildAt(2);
                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    } else if (text.getText().charAt(0) != color.charAt(0)) {
                                        theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                        text.setText("SPOT " + text.getText());
                                        setSpotOnClick(theLayout);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                                //System.out.println("Up and right");
                                for (int k = gridYPosition - 1, j = gridXPosition + 1; (k >= stoppingSpotYDownLeft && j <= stoppingSpotXDownLeft); k--, j++) {
                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                    TextView text = (TextView) theLayout.getChildAt(2);
                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    } else if (text.getText().charAt(0) != color.charAt(0)) {
                                        theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                        text.setText("SPOT " + text.getText());
                                        setSpotOnClick(theLayout);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
//                        //System.out.println("down and right");
                                for (int k = gridYPosition + 1, j = gridXPosition + 1; (k <= stoppingSpotYDownRight && j <= stoppingSpotXDownRight); k++, j++) {
                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                    TextView text = (TextView) theLayout.getChildAt(2);
                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    } else if (text.getText().charAt(0) != color.charAt(0)) {
                                        theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                        text.setText("SPOT " + text.getText());
                                        setSpotOnClick(theLayout);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
//                        //System.out.println("down and left");
                                for (int k = gridYPosition + 1, j = gridXPosition - 1; (k <= stoppingSpotYUpRight && j >= stoppingSpotXUpRight); k++, j--) {
                                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                    TextView text = (TextView) theLayout.getChildAt(2);
                                    if (text.getText().equals("")) {
                                        text.setText("SPOT");
                                        setSpotOnClick(theLayout);
                                        ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k))).getChildAt(0);
                                        image.setImageResource(R.drawable.chess_gotospot);
                                    } else if (text.getText().charAt(0) != color.charAt(0)) {
                                        theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                        text.setText("SPOT " + text.getText());
                                        setSpotOnClick(theLayout);
                                        break;
                                    } else {
                                        break;
                                    }
                                }

                                if (!inCheck) {
                                    if (gridYPosition - 1 >= 0) {
                                        for (int j = gridConverter.get(gridXPosition).indexOf(finalPosition - 1); j >= 0; j--) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                                //set spots onclick
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    if (gridYPosition + 1 <= 7) {
                                        //loop right
                                        for (int j = gridConverter.get(gridXPosition).indexOf(finalPosition + 1); j <= 7; j++) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(j))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
//                        //loop up
                                    if (gridXPosition - 1 >= 0) {
                                        int gridIndex = gridConverter.get(gridXPosition).indexOf(finalPosition);
                                        for (int j = gridXPosition - 1; j >= 0; j--) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridIndex));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridIndex))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    if (gridXPosition + 1 <= 7) {
                                        int gridIndex = gridConverter.get(gridXPosition).indexOf(finalPosition);
                                        for (int j = gridXPosition + 1; j <= 7; j++) {
                                            RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridIndex));
                                            TextView text = (TextView) theLayout.getChildAt(2);
                                            if (text.getText().equals("")) {
                                                text.setText("SPOT");
                                                setSpotOnClick(theLayout);
                                                ImageView image = (ImageView) ((RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(gridIndex))).getChildAt(0);
                                                image.setImageResource(R.drawable.chess_gotospot);
                                            } else if (text.getText().charAt(0) != color.charAt(0)) {
                                                theLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                                text.setText("SPOT " + text.getText());
                                                setSpotOnClick(theLayout);
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void setKingOnClick(RelativeLayout layout){
        final int finalPosition = findPosition(layout);
        resetGrid();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstClick.equals("KING"+finalPosition)){
                    //this piece
                    clearSpots();
                    resetGrid();
                    firstClick = "";
                    secondClick = "";
                }else {
                    clearSpots();
                    resetGrid();
                    firstClick = "KING" + finalPosition;
                    secondClick = "";
                    int[][] asArray = {{+1, 0}, {+1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, +1}, {0, +1}, {+1, +1}};
                    ArrayList<int[]> potentialPositions = new ArrayList<>(Arrays.asList(asArray));


                    for (int i = 0; i < gridConverter.size(); i++) {
                        if (gridConverter.get(i).contains(finalPosition)) {
                            int gridXPosition = i;
                            int gridYPosition = gridConverter.get(i).indexOf(finalPosition);
                            if (points[0][0] == -1) {
                                points[0][0] = gridXPosition;
                                points[0][1] = gridYPosition;
                            }
                            //hasKingBeenCastled;
                            if(!hasKingBeenCastled) {
                                if (!hasKingMoved) {
                                    for(int x=gridYPosition+1; x<=7;x++){
                                        RelativeLayout closeLeft = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(x));
                                        TextView closeLeftText = (TextView)closeLeft.getChildAt(2);
                                        if(x==7 && closeLeftText.getText().toString().contains("ROOK")){
                                            int[] array = {0, +2};
                                            potentialPositions.add(array);
                                        }else if(closeLeftText.getText().equals("")){
                                        }else{
                                            break;
                                        }
                                    }
                                    for(int x=gridYPosition-1; x>=0;x--){
                                        RelativeLayout closeLeft = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(x));
                                        TextView closeLeftText = (TextView)closeLeft.getChildAt(2);
                                        if(x==0 && closeLeftText.getText().toString().contains("ROOK")){
                                            int[] array = {0, -2};
                                            potentialPositions.add(array);
                                        }else if(closeLeftText.getText().equals("")){
                                        }else{
                                            break;
                                        }
                                    }
                                }
                            }
                            ArrayList<RelativeLayout> availableSpots = new ArrayList<>();
                            for (int[] possibleMove : potentialPositions) {
                                if ((gridXPosition + possibleMove[0] >= 0 && gridXPosition + possibleMove[0] <= 7) && (gridYPosition + possibleMove[1] >= 0 && gridYPosition + possibleMove[1] <= 7)) {
                                    int j = gridXPosition + possibleMove[0]; //X
                                    int k = gridYPosition + possibleMove[1]; //Y
                                    if (!anyPieceCanMoveToThisPiece(j, k, "ENEMY")) { //no piece can move to this spot
                                        RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                                        availableSpots.add(theLayout);
                                    }
                                }
                            }

                            for (RelativeLayout spot : availableSpots) {
                                TextView text = (TextView) spot.getChildAt(2);
                                if (text.getText().equals("")) {
                                    text.setText("SPOT");
                                    setSpotOnClick(spot);
                                    ImageView image = (ImageView) spot.getChildAt(0);
                                    image.setImageResource(R.drawable.chess_gotospot);
                                } else if (text.getText().charAt(0) != color.charAt(0)) {
                                    spot.setBackgroundColor(getResources().getColor(R.color.green));
                                    text.setText("SPOT " + text.getText());
                                    setSpotOnClick(spot);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public ArrayList<Integer> findPositionOfItemEnemyItems(String piece){
        int position = 0; //item number with the array (ie 45)
        ArrayList<Integer> foundPositions = new ArrayList<>();
        for(int i = 0; i<gridLayout.getChildCount(); i++){
            RelativeLayout layout = (RelativeLayout)gridLayout.getChildAt(i);
            TextView informationText = (TextView)layout.getChildAt(2);
            if(!informationText.getText().equals("")){
                if(informationText.getText().charAt(0) != color.charAt(0)) {
                    if(informationText.getText().toString().contains(piece)){
                        foundPositions.add(position);
                    }
                }
            }
            position++;
        }
        return foundPositions;
    } //used to find the enemy pieces

    public ArrayList<Integer> findPositionOfItemFriendlyItems(String piece){
        int position = 0; //item number with the array (ie 45)
        ArrayList<Integer> foundPositions = new ArrayList<>();
        for(int i = 0; i<gridLayout.getChildCount(); i++){
            RelativeLayout layout = (RelativeLayout)gridLayout.getChildAt(i);
            TextView informationText = (TextView)layout.getChildAt(2);
            if(!informationText.getText().equals("")){
                if(informationText.getText().charAt(0) == color.charAt(0)) {
                    if(informationText.getText().toString().contains(piece)){
                        foundPositions.add(position);
                    }
                }
            }
            position++;
        }
        return foundPositions;
    } //used to find the friendly pieces

    public boolean canRookMoveToSpot(int x, int y, String type, String playerType){
        ArrayList<Integer> foundPositions;
        if(playerType.equals("ENEMY")){
            foundPositions = findPositionOfItemEnemyItems(type);
        }else{
            foundPositions = findPositionOfItemFriendlyItems(type);
        }
        for(Integer inte: foundPositions){
            boolean myBool = canThisRookMoveToSpot(x,y,playerType,inte);
            if(!myBool){
                continue;
            }else{
                return myBool;
            }
        }
        return false;
    }

    public boolean canThisRookMoveToSpot(int x, int y, String playerType, int gridItem){
        for (int i = 0; i < gridConverter.size(); i++) {
            if (gridConverter.get(i).contains(gridItem)) {
                int gridXPosition = i;
                int gridYPosition = gridConverter.get(i).indexOf(gridItem);
                for(int k=gridXPosition+1; k<=x; k++) { //moving down
                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(k).get(gridYPosition));
                    TextView text = (TextView)theLayout.getChildAt(2);
                    if(!text.getText().equals("")){
                        if(x == k && y == gridYPosition && color.charAt(0)!=text.getText().toString().charAt(0)){
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }else{
                            break;
                        }
                    }else if (x == k && y == gridYPosition) {
                        if(playerType.equals("FRIENDLY")){
                            friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                        }
                        return true;
                    }

                }
                for(int k=gridXPosition-1; k>=x; k--) { //moving up
                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(k).get(gridYPosition));
                    TextView text = (TextView)theLayout.getChildAt(2);
                    if(!text.getText().equals("")){
                        if(x == k && y == gridYPosition && color.charAt(0)!=text.getText().toString().charAt(0)){
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }else{
                            break;
                        }
                    }else if (x == k && y == gridYPosition) {
                        if(playerType.equals("FRIENDLY")){
                            friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                        }
                        return true;
                    }
                }
                for(int k=gridYPosition-1; k>=y; k--) { //moving left
                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(k));
                    TextView text = (TextView)theLayout.getChildAt(2);
                    if(!text.getText().equals("")){
                        if(x == gridXPosition && y == k && color.charAt(0)!=text.getText().toString().charAt(0)){
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }else{
                            break;
                        }
                    }else if (x == gridXPosition && y == k) {
                        if(playerType.equals("FRIENDLY")){
                            friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                        }
                        return true;
                    }
                }
                for(int k=gridYPosition+1; k<=y; k++) { //moving down
                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition).get(k));
                    TextView text = (TextView)theLayout.getChildAt(2);
                    if(!text.getText().equals("")){
                        if(x == gridXPosition && y == k && color.charAt(0)!=text.getText().toString().charAt(0)){
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }else{
                            break;
                        }
                    }else if (x == gridXPosition && y == k) {
                        if(playerType.equals("FRIENDLY")){
                            friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canKnightMoveToSpot(int x, int y, String playerType){
        ArrayList<Integer> foundPositions;
        if(playerType.equals("ENEMY")){
            foundPositions = findPositionOfItemEnemyItems("KNIGHT");
        }else{
            foundPositions = findPositionOfItemFriendlyItems("KNIGHT");
        }
        for(Integer inte: foundPositions) {
            boolean myBool = canThisKnightMoveToSpot(x,y,playerType,inte);
            if(!myBool){
                continue;
            }else{
                return myBool;
            }
        }
        return false;
    }

    public boolean canThisKnightMoveToSpot(int x, int y, String playerType, int gridItem){
        for (int i = 0; i < gridConverter.size(); i++) {
            if (gridConverter.get(i).contains(gridItem)) {
                int gridXPosition = i;
                int gridYPosition = gridConverter.get(i).indexOf(gridItem);
                int[][] potentialPositions = {{2,1},{1,2},{-2,1},{-1,2},{-2,-1},{-1,-2},{2,-1},{1,-2}};
                for (int[] possibleMove : potentialPositions) {
                    if ((gridXPosition + possibleMove[0] >= 0 && gridXPosition + possibleMove[0] <= 7) && (gridYPosition + possibleMove[1] >= 0 && gridYPosition + possibleMove[1] <= 7)) {
                        int j = gridXPosition + possibleMove[0];
                        int k = gridYPosition + possibleMove[1];
                        if (x == j && y == k) {
                            System.out.println(gridItem+"KNIGHT MOVING TO SPOT + " + x +" " + y);
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean canBishopMoveToSpot(int x, int y, String type, String playerType){
        ArrayList<Integer> foundPositions;
        if(playerType.equals("ENEMY")){
            foundPositions = findPositionOfItemEnemyItems(type);
        }else{
            foundPositions = findPositionOfItemFriendlyItems(type);
        }
        for(Integer inte: foundPositions){
            boolean myBool = canThisBishopMoveToSpot(x,y,playerType,inte);
            if(!myBool){
                continue;
            }else{
                return myBool;
            }
        }
        return false;
    }

    public boolean canThisBishopMoveToSpot(int x, int y, String playerType, int gridItem){
        for (int i = 0; i < gridConverter.size(); i++) {
            if (gridConverter.get(i).contains(gridItem)) {
                int gridXPosition = i;
                int gridYPosition = gridConverter.get(i).indexOf(gridItem);
                //System.out.println(inte+"Up and left");
                for(int j=gridXPosition-1, k=gridYPosition-1;  (k>=0 && j>=0); j--,k--) {
                    //System.out.println("J: " + j  + " K:"+k);
                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                    TextView text = (TextView)theLayout.getChildAt(2);
                    if(!text.getText().equals("")){
                        if(x == j && y == k && color.charAt(0)!=text.getText().toString().charAt(0)){
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }else{
                            break;
                        }
                    }else if (x == j && y == k) {
                        if(playerType.equals("FRIENDLY")){
                            friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                        }
                        return true;
                    }
                }
                //System.out.println("down and left");
                for(int k=gridYPosition-1, j=gridXPosition+1; (k>=0 && j<=7); k--, j++) {
                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                    TextView text = (TextView)theLayout.getChildAt(2);
                    if(!text.getText().equals("")){
                        if(x == j && y == k && color.charAt(0)!=text.getText().toString().charAt(0)){
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }else{
                            break;
                        }
                    }else if (x == j && y == k) {
                        if(playerType.equals("FRIENDLY")){
                            friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                        }
                        return true;
                    }
                }
                //System.out.println("down and right");
                for(int k=gridYPosition+1, j=gridXPosition+1; (k<=7 && j<=7); k++, j++) {
                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                    TextView text = (TextView)theLayout.getChildAt(2);
                    if(!text.getText().equals("")){
                        if(x == j && y == k && color.charAt(0)!=text.getText().toString().charAt(0)){
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }else{
                            break;
                        }
                    }else if (x == j && y == k) {
                        if(playerType.equals("FRIENDLY")){
                            friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                        }
                        return true;
                    }
                }
                //System.out.println("up and right");
                for(int k=gridYPosition+1, j=gridXPosition-1; (k<=7 && j>=0); k++, j--) {
                    RelativeLayout theLayout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(j).get(k));
                    TextView text = (TextView)theLayout.getChildAt(2);
                    if(!text.getText().equals("")){
                        if(x == j && y == k && color.charAt(0)!=text.getText().toString().charAt(0)){
                            if(playerType.equals("FRIENDLY")){
                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                            }
                            return true;
                        }else{
                            break;
                        }
                    }else if (x == j && y == k) {
                        if(playerType.equals("FRIENDLY")){
                            friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canQueenMoveToSpot(int x, int y, String playerType){
        boolean rookMovement = canRookMoveToSpot(x,y,"QUEEN", playerType);
        boolean bishopMovement = canBishopMoveToSpot(x,y,"QUEEN", playerType);
        return rookMovement||bishopMovement;
    }

    public boolean canThisQueenMoveToSpot(int x, int y, String playerType, int gridItem){
        boolean rookMovement = canThisRookMoveToSpot(x,y, playerType,gridItem);
        boolean bishopMovement = canThisBishopMoveToSpot(x,y, playerType, gridItem);
        return rookMovement||bishopMovement;
    }

    public boolean canThisPawnMove(int x, int y, int gridX, int gridY){//x , y finishing spots, gridx, gridy Starting spots
        int gridXPosition = gridX;
        int gridYPosition = gridY;
        if (gridXPosition == 6) {//6 so check if can move 2 forward to block point
            int[][] asArray = {{-1, 0}, {-1, -1}, {-1,+1}, {-2, 0}};
            for(int[] array: asArray) {
                if(gridXPosition+array[0]>=0 && gridXPosition+array[0]<=7 && gridYPosition+array[1]>=0 && gridYPosition+array[1]<=7) {
                    if(gridXPosition+array[0]==x && gridYPosition+array[1]==y){
                        RelativeLayout layout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition+array[0]).get(gridYPosition+array[1]));
                        TextView textOfPiece = (TextView)layout.getChildAt(2);
                        if(array[0]==-1&&array[1]==-1&&textOfPiece.getText().equals("")){
                            //break;
                        }else if(array[0]==-1&&array[1]==+1&&textOfPiece.getText().equals("")){
                            // break;
                        }else{
                            return true;
                        }
                    }
                }
            }
        }else {
            int[][] asArray = {{-1, 0}, {-1, -1}, {-1,+1}};
            for(int[] array: asArray) {
                if(gridXPosition+array[0]>=0 && gridXPosition+array[0]<=7 && gridYPosition+array[1]>=0 && gridYPosition+array[1]<=7) {
                    if(gridXPosition+array[0]==x && gridYPosition+array[1]==y){
                        RelativeLayout layout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition+array[0]).get(gridYPosition+array[1]));
                        TextView textOfPiece = (TextView)layout.getChildAt(2);
                        if(array[0]==-1&&array[1]==-1&&textOfPiece.getText().equals("")){
                            //break;
                        }else if(array[0]==-1&&array[1]==+1&&textOfPiece.getText().equals("")){
                            // break;
                        }else{
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean canPawnMoveToSpot(int x, int y, String playerType){
        //pawns are only a threat if can move diagonally to spot
        ArrayList<Integer> foundPositions;
        boolean success = false;
        if(playerType.equals("ENEMY")){
            foundPositions = findPositionOfItemEnemyItems("PAWN");
            for(Integer inte: foundPositions){
                //System.out.println("PAWNS: " + inte);
                for (int i = 0; i < gridConverter.size(); i++) {
                    if (gridConverter.get(i).contains(inte)) {
                        int gridXPosition = i;
                        int gridYPosition = gridConverter.get(i).indexOf(inte);
                        if (gridXPosition + 1 == x && gridYPosition - 1 == y) {
                            System.out.println("PAWN MOVING DOWN AND LEFT");
                            return true;
                        } else if (gridXPosition + 1 == x && gridYPosition + 1 == y) {
                            System.out.println("PAWN MOVING DOWN AND RIGHT");
                            return true;
                        }
                    }
                }
            }
        }else {
            foundPositions = findPositionOfItemFriendlyItems("PAWN");
            for (Integer inte : foundPositions) {
                //System.out.println("PAWNS: " + inte);
                for (int i = 0; i < gridConverter.size(); i++) {
                    if (gridConverter.get(i).contains(inte)) {
                        int gridXPosition = i;
                        int gridYPosition = gridConverter.get(i).indexOf(inte);
                        if (gridXPosition == 6) {//6 so check if can move 2 forward to block point
                            int[][] asArray = {{-1, 0}, {-1, -1}, {-1,+1}, {-2, 0}};
                            for(int[] array: asArray) {
                                if(gridXPosition+array[0]>=0 && gridXPosition+array[0]<=7 && gridYPosition+array[1]>=0 && gridYPosition+array[1]<=7) {
                                    if(gridXPosition+array[0]==x && gridYPosition+array[1]==y){
                                        RelativeLayout layout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition+array[0]).get(gridYPosition+array[1]));
                                        TextView textOfPiece = (TextView)layout.getChildAt(2);
                                        if(array[0]==-1 && array[1]==-1){
                                            if(!textOfPiece.getText().equals("")){
                                                if(textOfPiece.getText().toString().charAt(0) != color.charAt(0)){
                                                    if(playerType.equals("FRIENDLY")){
                                                        friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                                                    }
                                                    success = true;
                                                }
                                            }

                                            //break;
                                        }else if(array[0]==-1&&array[1]==+1){
                                            if(!textOfPiece.getText().equals("")){
                                                if(textOfPiece.getText().toString().charAt(0) != color.charAt(0)){
                                                    if(playerType.equals("FRIENDLY")){
                                                        friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                                                    }
                                                    success=true;
                                                }
                                            }
                                        }else{
                                            if(playerType.equals("FRIENDLY")){
                                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                                            }
                                            success = true;
                                        }
                                    }
                                }
                            }
                        }else{
                            int[][] asArray = {{-1, 0}, {-1, -1}, {-1,+1}};
                            for(int[] array: asArray) {
                                if(gridXPosition+array[0]>=0 && gridXPosition+array[0]<=7 && gridYPosition+array[1]>=0 && gridYPosition+array[1]<=7) {
                                    if(gridXPosition+array[0]==x && gridYPosition+array[1]==y){
                                        RelativeLayout layout = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(gridXPosition+array[0]).get(gridYPosition+array[1]));
                                        TextView textOfPiece = (TextView)layout.getChildAt(2);
                                        if(array[0]==-1&&array[1]==-1){
                                            if(!textOfPiece.getText().equals("")){
                                                if(textOfPiece.getText().toString().charAt(0) != color.charAt(0)){
                                                    if(playerType.equals("FRIENDLY")){
                                                        friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                                                    }
                                                    success = true;
                                                }
                                            }
                                            //break;
                                        }else if(array[0]==-1&&array[1]==+1){
                                            if(!textOfPiece.getText().equals("")){
                                                if(textOfPiece.getText().toString().charAt(0) != color.charAt(0)){
                                                    if(playerType.equals("FRIENDLY")){
                                                        friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                                                    }
                                                    success = true;
                                                }
                                            }
                                            // break;
                                        }else{
                                            if(playerType.equals("FRIENDLY")){
                                                friendlyPiecesThatCanMoveInCheck.add(gridConverter.get(gridXPosition).get(gridYPosition));
                                            }
                                            success = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return success;

    }

    public boolean anyPieceCanMoveToThisPiece(int x, int y, String playerType){
        boolean pawn = canPawnMoveToSpot(x,y,playerType);
        boolean knight = canKnightMoveToSpot(x,y,playerType);
        boolean rook = canRookMoveToSpot(x,y, "ROOK",playerType);
        boolean bishop = canBishopMoveToSpot(x,y,"BISHOP",playerType);
        boolean queen = canQueenMoveToSpot(x,y,playerType);

        return rook || knight || bishop || queen || pawn;
    }

    public int findKingByName(){
        for(int i = 0; i<gridLayout.getChildCount(); i++) {
            RelativeLayout kingLayout = (RelativeLayout) gridLayout.getChildAt(i);
            TextView kingText = (TextView) kingLayout.getChildAt(2);
            if(kingText.getText().toString().contains("KING") && kingText.getText().toString().charAt(0) == color.charAt(0)){
                //found king
                return i;
            }
        }
        return -1;
    }

    public ArrayList<int[]> findCheckingPiece(){
        //loop from king in all direction until encountered a enemy piece
        int kingSelectionNumber = findKingByName();
        int kingX = -1;
        int kingY = -1;
        ArrayList<int[]> points = new ArrayList<>(); //HOLDS THE POINTS ON WHICH A MOVE MUST BE MADE, OR KING MOVED
        for (int i = 0; i < gridConverter.size(); i++) {
            if (gridConverter.get(i).contains(kingSelectionNumber)) {
                int gridXPosition = i;
                int gridYPosition = gridConverter.get(i).indexOf(kingSelectionNumber);
                kingX = gridXPosition;
                kingY = gridYPosition;
            }
        }
        if(kingX!=-1 && kingY!=-1) {
            int[][] potentialPositions = {{+1, 0}, {+1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, +1}, {0, +1}, {+1, +1}};
            int count = 0;
            ArrayList<Integer> directionPieceWasFound = new ArrayList<>();
            for(int[] position: potentialPositions){
                for(int j = kingX+position[0], k = kingY+position[1]; ((k >= 0 && k<=7) && (j >= 0 && j<=7)); j+=position[0], k+=position[1]){
                    //System.out.println("POSITION" + count + "  j:" + j + "  k:"+k);
                    int gridLayoutPosition = gridConverter.get(j).get(k);
                    RelativeLayout layout = (RelativeLayout)gridLayout.getChildAt(gridLayoutPosition);
                    TextView text = (TextView)layout.getChildAt(2);
                    if(!text.getText().toString().equals("")){
                        if(text.getText().toString().charAt(0) != color.charAt(0)){
                            directionPieceWasFound.add(count);
                            break;
                        }else{
                            break;
                        }
                    }
                }
                count++;
            }
            //ArrayList<int[]> directionKnightWasFound = new ArrayList<>();
            int[][] knightPositions = {{2,1},{1,2},{-2,1},{-1,2},{-2,-1},{-1,-2},{2,-1},{1,-2}};
            for(int[] knightPosition: knightPositions){
                if(kingX+knightPosition[0]<=7 && kingX+knightPosition[0]>=0 && kingY+knightPosition[1]<=7 && kingY+knightPosition[1]>=0){
                    int gridLayoutPosition = gridConverter.get(kingX+knightPosition[0]).get(kingY+knightPosition[1]);
                    RelativeLayout layout = (RelativeLayout)gridLayout.getChildAt(gridLayoutPosition);
                    TextView text = (TextView)layout.getChildAt(2);
                    if(!text.getText().toString().equals("")) {
                        if (text.getText().toString().charAt(0) != color.charAt(0)) {
                            int [] array = new int[2];
                            array[0] = kingX+knightPosition[0];
                            array[1] = kingY+knightPosition[1];
                            points.add(array);
                        }
                    }
                }
            }

            for(Integer inte: directionPieceWasFound){
                for(int j = kingX+potentialPositions[inte][0], k = kingY+potentialPositions[inte][1]; ((k >= 0 && k<=7) && (j >= 0 && j<=7)); j+=potentialPositions[inte][0], k+=potentialPositions[inte][1]){
                    int gridLayoutPosition = gridConverter.get(j).get(k);
                    RelativeLayout layout = (RelativeLayout)gridLayout.getChildAt(gridLayoutPosition);
                    TextView text = (TextView)layout.getChildAt(2);
                    if(!text.getText().toString().equals("")) {
                        if (text.getText().toString().charAt(0) != color.charAt(0)) {
                            int [] array = new int[2];
                            array[0] = j;
                            array[1] = k;
                            points.add(array);
                        }
                        break;
                    }else{
                        int [] array = new int[2];
                        array[0] = j;
                        array[1] = k;
                        points.add(array);
                    }

                }
            }
        }
//        for(int[] array: points){
//            System.out.println(Arrays.toString(array));
//        }
        return points;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = new Intent(this, ServiceClass.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Chess");
        Intent fromintent = getIntent();
        color = fromintent.getStringExtra("COLOR");
        String yourName = fromintent.getStringExtra("YOURNAME");
        String enemyUserName = fromintent.getStringExtra("ENEMYNAME");
        topLayout = (RelativeLayout)findViewById(R.id.enemyMove);
        TextView enemyName = (TextView)topLayout.getChildAt(0);
        enemyName.setText(enemyUserName);
        bottomLayout = (RelativeLayout)findViewById(R.id.myMove);
        TextView myName = (TextView)bottomLayout.getChildAt(0);
        myName.setText(myName.getText() + "  ("+yourName+")");
        deadPiecesLayout = findViewById(R.id.deadPiecesLayout);
        RelativeLayout forfeit = findViewById(R.id.forfeitLayout);
        forfeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Game.this)
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to forfeit")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Game.super.onBackPressed();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                        .create().show();
            }
        });
        gridLayout = findViewById(R.id.gridlayout);
        firstClick = "";
        secondClick = "";
        friendlyPiecesThatCanMoveInCheck = new ArrayList<>();
        checkedSpots = new ArrayList<>();
        for(int i = 0; i<=56; i+=8){
            ArrayList<Integer> content = new ArrayList<>();
            for(int j = i; j<i+8; j++){
                content.add(j);
            }
            gridConverter.add(content);
        }
        if(gridLayout!=null) {
            if (color.equals("BLACK")) {
                for(int i=8; i<=15; i++){
                    RelativeLayout view = (RelativeLayout)gridLayout.getChildAt(i);
                    ImageView imageView = (ImageView)view.getChildAt(0);
                    imageView.setImageResource(R.drawable.chess_plt60);
                    ((TextView)view.getChildAt(2)).setText("WPAWN");
                }
                int[] whiteArray = {0,1,2,3,4,5,6,7};
                int[] blackArray = {56,57,58,59,60,61,62,63};
                setBlacksGrid(blackArray,whiteArray);
                for(int i=48; i<=55; i++){
                    RelativeLayout view = (RelativeLayout)gridLayout.getChildAt(i);
                    ImageView imageView = (ImageView)view.getChildAt(0);
                    imageView.setImageResource(R.drawable.chess_pdt60);
                    ((TextView)view.getChildAt(2)).setText("BPAWN");

                }

            } else if (color.equals("WHITE")) {
                for(int i=8; i<=15; i++){
                    RelativeLayout view = (RelativeLayout)gridLayout.getChildAt(i);
                    ImageView imageView = (ImageView)view.getChildAt(0);
                    imageView.setImageResource(R.drawable.chess_pdt60);
                    ((TextView)view.getChildAt(2)).setText("BPAWN");

                }
                int[] whiteArray = {56,57,58,59,60,61,62,63};
                int[] blackArray = {0,1,2,3,4,5,6,7};
                setWhitesGrid(whiteArray,blackArray);
                for(int i=48; i<=55; i++){
                    RelativeLayout view = (RelativeLayout)gridLayout.getChildAt(i);
                    ImageView imageView = (ImageView)view.getChildAt(0);
                    imageView.setImageResource(R.drawable.chess_plt60);
                    setPawnOnClick(view);
                    ((TextView)view.getChildAt(2)).setText("WPAWN");

                }
            }
        }
        int count = -1;
        if(color.equals("WHITE")){
            topLayout.setBackgroundColor(getResources().getColor(R.color.grey));
            bottomLayout.setBackgroundColor(getResources().getColor(R.color.green));

            count = 8;
        }else{
            bottomLayout.setBackgroundColor(getResources().getColor(R.color.grey));
            topLayout.setBackgroundColor(getResources().getColor(R.color.green));

            count = 1;
        }
        String letters = "abcdefgh";
        if(gridLayout!=null){
            for (int i = 7; i < gridLayout.getChildCount(); i+=8) {
                RelativeLayout childRelativeLayout = (RelativeLayout) gridLayout.getChildAt(i);
                if (color.equals("WHITE")) {
                    //counting down
                    if(i==63){
                        TextView informationBox = (TextView) childRelativeLayout.getChildAt(3);
                        informationBox.setText("" + count);
                    }else{
                        TextView informationBox = (TextView) childRelativeLayout.getChildAt(1);
                        informationBox.setText("" + count);
                        count--;
                    }
                } else {
                    //counting up
                    if(i==63){
                        TextView informationBox = (TextView) childRelativeLayout.getChildAt(3);
                        informationBox.setText("" + count);
                    }else{
                        TextView informationBox = (TextView) childRelativeLayout.getChildAt(1);
                        informationBox.setText("" + count);
                        count++;
                    }
                }
            }
            //one is the bottom
            for (int i = 56; i < gridLayout.getChildCount(); i++) {
                RelativeLayout childRelativeLayout = (RelativeLayout) gridLayout.getChildAt(i);
                if (color.equals("WHITE")) {
                    //counting down
                    TextView informationBox = (TextView) childRelativeLayout.getChildAt(1);
                    informationBox.setText(""+letters.charAt(i - 56));
                } else {
                    //counting up
                    TextView informationBox = (TextView) childRelativeLayout.getChildAt(1);
                    informationBox.setText(""+letters.charAt(Math.abs(i-55-8)));
                }
            }


        }
    }

    public String[] convertToChessCoordinates(String startX, String startY, String finishX, String finishY){
        String[] array = new String[2];
        String letters = "abcdefgh";
        String start = ""+letters.charAt(Integer.parseInt(startY))+""+Math.abs(Integer.parseInt(startX)-8);
        String finish =  ""+letters.charAt(Integer.parseInt(finishY))+""+Math.abs(Integer.parseInt(finishX)-8);
        array[0] = start;
        array[1] = finish;
        return array;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
//        unbindService(connection);
//        mBound = false;
    }

    public void changeColor(){
        Drawable layoutDrawable = topLayout.getBackground();
        String layoutColor;
        if(layoutDrawable instanceof ColorDrawable){
            int colorOfBar = ((ColorDrawable)layoutDrawable).getColor();
            int[] rainbow = getResources().getIntArray(R.array.colors);
            if(colorOfBar == rainbow[0]){
                topLayout.setBackgroundColor(getResources().getColor(R.color.grey));
                bottomLayout.setBackgroundColor(getResources().getColor(R.color.green));
                for(int i = 0; i<gridLayout.getChildCount(); i++) {
                    RelativeLayout currentChild = (RelativeLayout)gridLayout.getChildAt(i);
                    TextView pieceIdentifier = (TextView)currentChild.getChildAt(2);
                    if(!pieceIdentifier.getText().equals("")){
                        if(pieceIdentifier.getText().charAt(0) == color.charAt(0)) {
                            switch (pieceIdentifier.getText().toString().substring(1)) {
                                case "ROOK":
                                    setRookOnClick(currentChild);
                                    break;
                                case "KNIGHT":
                                    setKnightOnClick(currentChild);
                                    break;
                                case "BISHOP":
                                    setBishopOnClick(currentChild);
                                    break;
                                case "QUEEN":
                                    setQueenOnClick(currentChild);
                                    break;
                                case "KING":
                                    setKingOnClick(currentChild);
                                    break;
                                case "PAWN":
                                    setPawnOnClick(currentChild);
                                    break;
                            }
                        }
                    }
                }

            }else{
                topLayout.setBackgroundColor(getResources().getColor(R.color.green));
                bottomLayout.setBackgroundColor(getResources().getColor(R.color.grey));
                for(int i = 0; i<gridLayout.getChildCount(); i++) {
                    RelativeLayout currentChild = (RelativeLayout) gridLayout.getChildAt(i);
                    currentChild.setOnClickListener(null);
                }
            }
        }
    }

    @Subscribe
    public void onEvent(EventClass eventClass){
        if(!eventClass.fromClass.equals("ACTIVITY")){
            String[] messageArray = eventClass.message.split(" ");
            if(messageArray[0].equals("CONNECTION_MADE")){
                //DIALOG CLOSE
                EventBus.getDefault().post(new EventClass("ACTIVITY", "LEAVE_THREAD"));
                Intent dialogIntent = new Intent(this, Game.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogIntent.putExtra("COLOR", messageArray[1]);
                startActivity(dialogIntent);

            }else if(messageArray[0].equals("PROMOTE")) {
                final Dialog dialog = new Dialog(this);
                dialog.setCanceledOnTouchOutside(false);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.pawn_promotion, null);
                dialogView.findViewById(R.id.cardview1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "PROMOTE_TO QUEEN"));
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.cardview2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "PROMOTE_TO KNIGHT"));
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.cardview3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "PROMOTE_TO ROOK"));
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.cardview4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventClass("ACTIVITY", "PROMOTE_TO BISHOP"));
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            }else if(messageArray[0].equals("PROMOTION")){
                final String[] piecePosition = messageArray[1].split(",");
                int piecePositionInGrid;
                if(color.equals("BLACK")){
                    piecePositionInGrid = (Math.abs(Integer.parseInt(piecePosition[0])-7) * 8) + Math.abs(7-Integer.parseInt(piecePosition[1]));
                }else {
                    piecePositionInGrid = (Integer.parseInt(piecePosition[0]) * 8) + Integer.parseInt(piecePosition[1]);
                }
                final String receivedPieceColor = messageArray[2];
                RelativeLayout viewLayout = (RelativeLayout) gridLayout.getChildAt(piecePositionInGrid);
                ImageView view2 = (ImageView) viewLayout.getChildAt(0);
                TextView informationText = (TextView) viewLayout.getChildAt(2);
                switch (messageArray[3]){
                    case "QUEEN":
                        if(receivedPieceColor.equals("WHITE")){
                            view2.setImageResource(R.drawable.chess_qlt60);
                            informationText.setText("WQUEEN");
                        }else if(receivedPieceColor.equals("BLACK")){
                            view2.setImageResource(R.drawable.chess_qdt60);
                            informationText.setText("BQUEEN");
                        }
                        break;
                    case "KNIGHT":
                        if(receivedPieceColor.equals("WHITE")){
                            view2.setImageResource(R.drawable.chess_nlt60);
                            informationText.setText("WKNIGHT");
                        }else if(receivedPieceColor.equals("BLACK")){
                            view2.setImageResource(R.drawable.chess_ndt60);
                            informationText.setText("BKNIGHT");
                        }
                        break;

                    case "ROOK":
                        if(receivedPieceColor.equals("WHITE")){
                            view2.setImageResource(R.drawable.chess_rlt60);
                            informationText.setText("WROOK");
                        }else if(receivedPieceColor.equals("BLACK")) {
                            view2.setImageResource(R.drawable.chess_rdt60);
                            informationText.setText("BROOK");
                        }
                        break;
                    case "BISHOP":
                        if(receivedPieceColor.equals("WHITE")){
                            view2.setImageResource(R.drawable.chess_blt60);
                            informationText.setText("WBISHOP");
                        }else if(receivedPieceColor.equals("BLACK")){
                            view2.setImageResource(R.drawable.chess_bdt60);
                            informationText.setText("BBISHOP");
                        }
                        break;
                }

            }else if(messageArray[0].equals("CASTLING")){
                String[] kingStart = messageArray[1].split(",");
                String[] kingEnd = messageArray[2].split(",");
                String[] rookStart = messageArray[3].split(",");
                String[] rookEnd = messageArray[4].split(",");

                int kingGridLayoutStart;
                int kingGridLayoutEnd;
                int rookGridLayoutStart;
                int rookGridLayoutEnd;

                if(color.equals("BLACK")){
                    int kingGridPositionStart0 = Math.abs(Integer.parseInt(kingStart[0]) - 7);
                    int kingGridPositionStart1 = Math.abs(7 - Integer.parseInt(kingStart[1]));
                    int kingGridPositionFinish0 = Math.abs(Integer.parseInt(kingEnd[0]) - 7);
                    int kingGridPositionFinish1 = Math.abs(7 - Integer.parseInt(kingEnd[1]));

                    int rookGridPositionStart0 = Math.abs(Integer.parseInt(rookStart[0]) - 7);
                    int rookGridPositionStart1 = Math.abs(7 - Integer.parseInt(rookStart[1]));
                    int rookGridPositionFinish0 = Math.abs(Integer.parseInt(rookEnd[0]) - 7);
                    int rookGridPositionFinish1 = Math.abs(7 - Integer.parseInt(rookEnd[1]));

                    kingGridLayoutStart = (kingGridPositionStart0 * 8) + kingGridPositionStart1;
                    kingGridLayoutEnd = (kingGridPositionFinish0 * 8) + kingGridPositionFinish1;
                    rookGridLayoutStart = (rookGridPositionStart0 * 8) + rookGridPositionStart1;
                    rookGridLayoutEnd = (rookGridPositionFinish0 * 8) + rookGridPositionFinish1;
                }else{
                    kingGridLayoutStart = (Integer.parseInt(kingStart[0]) * 8) + Integer.parseInt(kingStart[1]);
                    kingGridLayoutEnd = (Integer.parseInt(kingEnd[0]) * 8) + Integer.parseInt(kingEnd[1]);
                    rookGridLayoutStart = (Integer.parseInt(rookStart[0]) * 8) + Integer.parseInt(rookStart[1]);
                    rookGridLayoutEnd = (Integer.parseInt(rookEnd[0]) * 8) + Integer.parseInt(rookEnd[1]);
                }

                hasKingBeenCastled = true;
                TextView lastMove = (TextView) findViewById(R.id.textView67);
                String[] contents = convertToChessCoordinates(kingStart[0],kingStart[1],kingEnd[0],kingEnd[1]);
                String[] contents2 = convertToChessCoordinates(rookStart[0],rookStart[1],rookEnd[0],rookEnd[1]);

                lastMove.setText(contents2[0] + "-" + contents2[1] + "   "+ contents[0] + "-" + contents[1] + "   " +lastMove.getText() );

                RelativeLayout viewKingLayout = (RelativeLayout) gridLayout.getChildAt(kingGridLayoutStart);
                RelativeLayout viewKing2Layout = (RelativeLayout) gridLayout.getChildAt(kingGridLayoutEnd);
                ImageView viewKing = (ImageView) viewKingLayout.getChildAt(0);
                ImageView view2King = (ImageView) viewKing2Layout.getChildAt(0);
                view2King.setImageDrawable(viewKing.getDrawable());
                viewKing.setImageResource(android.R.color.transparent);
                TextView initialKingText = (TextView)viewKingLayout.getChildAt(2);
                TextView finalKingText = (TextView)viewKing2Layout.getChildAt(2);
                finalKingText.setText(initialKingText.getText());
                initialKingText.setText("");
                viewKingLayout.setOnClickListener(null);

                RelativeLayout viewRookLayout = (RelativeLayout) gridLayout.getChildAt(rookGridLayoutStart);
                RelativeLayout viewRook2Layout = (RelativeLayout) gridLayout.getChildAt(rookGridLayoutEnd);
                ImageView viewRook = (ImageView) viewRookLayout.getChildAt(0);
                ImageView view2Rook = (ImageView) viewRook2Layout.getChildAt(0);
                view2Rook.setImageDrawable(viewRook.getDrawable());
                viewRook.setImageResource(android.R.color.transparent);
                TextView initialRookText = (TextView)viewRookLayout.getChildAt(2);
                TextView finalRookText = (TextView)viewRook2Layout.getChildAt(2);
                finalRookText.setText(initialRookText.getText());
                initialRookText.setText("");
                viewRookLayout.setOnClickListener(null);
                changeColor();

            }else if(messageArray[0].equals("SUCCESS")){
                final String[] firstPostion = messageArray[1].split(",");
                final String[] secondPosition = messageArray[2].split(",");
                int start;
                int finish;
                if(color.equals("BLACK")){
                    int gridPositionStart0 = Math.abs(Integer.parseInt(firstPostion[0]) - 7);
                    int gridPositionStart1 = Math.abs(7 - Integer.parseInt(firstPostion[1]));
                    int gridPositionFinish0 = Math.abs(Integer.parseInt(secondPosition[0]) - 7);
                    int gridPositionFinish1 = Math.abs(7 - Integer.parseInt(secondPosition[1]));


                    start = (gridPositionStart0 * 8) + gridPositionStart1;
                    finish = (gridPositionFinish0 * 8) + gridPositionFinish1;
                }else{
                    start = (Integer.parseInt(firstPostion[0]) * 8) + Integer.parseInt(firstPostion[1]);
                    finish = (Integer.parseInt(secondPosition[0]) * 8) + Integer.parseInt(secondPosition[1]);
                }
                if(inCheck){
                    inCheck = false;
                }

                TextView lastMove = (TextView) findViewById(R.id.textView67);
                String[] contents = convertToChessCoordinates(firstPostion[0],firstPostion[1],secondPosition[0],secondPosition[1]);

                lastMove.setText(contents[0] + "-" + contents[1] + "   " + lastMove.getText());
                RelativeLayout initialPositionLayout = (RelativeLayout) gridLayout.getChildAt(start);
                RelativeLayout finalLayoutPosition = (RelativeLayout) gridLayout.getChildAt(finish);
                TextView initialPieceText = (TextView)initialPositionLayout.getChildAt(2);
                TextView finalPieceText = (TextView)finalLayoutPosition.getChildAt(2);
                ImageView initialImage = (ImageView)initialPositionLayout.getChildAt(0);
                ImageView finalImage = (ImageView)finalLayoutPosition.getChildAt(0);

                System.out.println("INITIAL PIECE TEXT = " + initialPieceText.getText().toString());
                System.out.println("FINALY PIECE TEXT = " + finalPieceText.getText().toString());

                if(initialPieceText.getText().toString().contains("PAWN")){
                    //intiial piece is a pawn
                    //check that its doing en passant
                    if(finalPieceText.getText().toString().equals("")){
                        //final spot is blank
                        int finishXPosition = 0;
                        int finishYPosition = 0;

                        //X Y of end position
                        for (int i = 0; i < gridConverter.size(); i++) {
                            if (gridConverter.get(i).contains(finish)) {
                                finishXPosition = i;
                                finishYPosition = gridConverter.get(i).indexOf(finish);
                            }
                        }

                        int startXPosition = 0;
                        int startYPosition = 0;

                        //X Y of end position
                        for (int i = 0; i < gridConverter.size(); i++) {
                            if (gridConverter.get(i).contains(start)) {
                                startXPosition = i;
                                startYPosition = gridConverter.get(i).indexOf(start);
                            }
                        }


//                        if(startYPosition + 1 == finishYPosition || startYPosition - 1 == finishYPosition){ //going diagonal
//                            int pieceXToTake = ((startXPosition+1 == finishXPosition) ? startXPosition+1 : startXPosition-1);
//                            int pieceYToTake = finishYPosition;
//
//                            RelativeLayout pawnToTake = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(pieceXToTake).get(pieceYToTake));
//                            TextView pawnToTakeText = (TextView)pawnToTake.getChildAt(2);
//                            ImageView pawnImage = (ImageView)pawnToTake.getChildAt(0);
//                            if(pawnToTakeText.getText().toString().contains("PAWN")){
//                                ImageView deadPiece = new ImageView(getApplicationContext());
//                                deadPiece.setImageDrawable(pawnImage.getDrawable());
//                                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(100,100);
//                                deadPiece.setLayoutParams(lp);
//                                deadPiecesLayout.addView(deadPiece, 0);
//                                pawnToTakeText.setText("");
//                                pawnImage.setImageResource(android.R.color.transparent);
////                            }
////                        }

                        //moving diagonal
                        if(startXPosition + 1 == finishXPosition){//going forward
                            if(startYPosition + 1 == finishYPosition || startYPosition - 1 == finishYPosition) {
                                int pieceXToTake = startXPosition;
                                int pieceYToTake = finishYPosition;
                                RelativeLayout pawnToTake = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(pieceXToTake).get(pieceYToTake));
                                TextView pawnToTakeText = (TextView)pawnToTake.getChildAt(2);
                                ImageView pawnImage = (ImageView)pawnToTake.getChildAt(0);
                                if(pawnToTakeText.getText().toString().contains("PAWN")){
                                    ImageView deadPiece = new ImageView(getApplicationContext());
                                    deadPiece.setImageDrawable(pawnImage.getDrawable());
                                    FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(100,100);
                                    deadPiece.setLayoutParams(lp);
                                    deadPiecesLayout.addView(deadPiece, 0);
                                    pawnToTakeText.setText("");
                                    pawnImage.setImageResource(android.R.color.transparent);
                                }
                            }
                        }else if (startXPosition - 1 == finishXPosition){
                            if(startYPosition + 1 == finishYPosition || startYPosition - 1 == finishYPosition) {
                                int pieceXToTake = startXPosition;
                                int pieceYToTake = finishYPosition;
                                RelativeLayout pawnToTake = (RelativeLayout) gridLayout.getChildAt(gridConverter.get(pieceXToTake).get(pieceYToTake));
                                TextView pawnToTakeText = (TextView)pawnToTake.getChildAt(2);
                                ImageView pawnImage = (ImageView)pawnToTake.getChildAt(0);
                                if(pawnToTakeText.getText().toString().contains("PAWN")){
                                    ImageView deadPiece = new ImageView(getApplicationContext());
                                    deadPiece.setImageDrawable(pawnImage.getDrawable());
                                    FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(100,100);
                                    deadPiece.setLayoutParams(lp);
                                    deadPiecesLayout.addView(deadPiece, 0);
                                    pawnToTakeText.setText("");
                                    pawnImage.setImageResource(android.R.color.transparent);
                                }
                            }
                        }
                    }
                }

                if(!finalPieceText.getText().toString().equals("")){
                    ImageView deadPiece = new ImageView(getApplicationContext());
                    deadPiece.setImageDrawable(finalImage.getDrawable());
                    FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(100,100);
                    deadPiece.setLayoutParams(lp);
                    deadPiecesLayout.addView(deadPiece, 0);
                }
                finalImage.setImageDrawable(initialImage.getDrawable());
                initialImage.setImageResource(android.R.color.transparent);
                finalPieceText.setText(initialPieceText.getText());
                initialPieceText.setText("");
                initialPositionLayout.setOnClickListener(null);
                lastMoveMadeByPlayer = start + " " + finish;
                //Log.e("LAST MOVE LAYOUT FORM", ""+ lastMoveMadeByPlayer);
                changeColor();



                //DIALOG CLOSE
            }else if(messageArray[0].equals("CHECK")){
                if(messageArray[1].equals(color)){
                    checkedSpots = new ArrayList<>();
                    checkedSpots = findCheckingPiece();
                    friendlyPiecesThatCanMoveInCheck = new ArrayList<>();
                    inCheck = true;
                    for(int[] position: checkedSpots){
                        anyPieceCanMoveToThisPiece(position[0], position[1], "FRIENDLY");
                    }
                    for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        RelativeLayout imageView = (RelativeLayout)gridLayout.getChildAt(i);
                        imageView.setOnClickListener(null);
                    }

                    for(Integer position: friendlyPiecesThatCanMoveInCheck){
                        System.out.println(position);
                        RelativeLayout layoutOfMoveableItem = (RelativeLayout)gridLayout.getChildAt(position);
                        TextView text = (TextView)layoutOfMoveableItem.getChildAt(2);
                        switch (text.getText().toString().substring(1)) {
                            case "ROOK":
                                setRookOnClick(layoutOfMoveableItem);
                                break;
                            case "KNIGHT":
                                setKnightOnClick(layoutOfMoveableItem);
                                break;
                            case "BISHOP":
                                setBishopOnClick(layoutOfMoveableItem);
                                break;
                            case "QUEEN":
                                setQueenOnClick(layoutOfMoveableItem);
                                break;
                            case "PAWN":
                                setPawnOnClick(layoutOfMoveableItem);
                                break;
                        }
                    }
                    int king = findKingByName();
                    RelativeLayout layoutOfKing = (RelativeLayout)gridLayout.getChildAt(king);
                    setKingOnClick(layoutOfKing);

                }
                Toast.makeText(this, "CHECK!", Toast.LENGTH_LONG).show();
            }else if(messageArray[0].equals("CHECKMATE")){
                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                    RelativeLayout imageView = (RelativeLayout)gridLayout.getChildAt(i);
                    imageView.setOnClickListener(null);
                }
                
                topLayout.setBackgroundColor(getResources().getColor(R.color.red));
                bottomLayout.setBackgroundColor(getResources().getColor(R.color.red));


                Toast.makeText(this, "CHECKMATE!", Toast.LENGTH_LONG).show();
            } else if(messageArray[0].equals("FAILURE")){
                Toast.makeText(this, "MOVE FAILURE", Toast.LENGTH_LONG).show();
            } else if(messageArray[0].equals("DISCONNECTFROMSERVER")){
//                AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
//                AlertDialog dialog = builder.create();
//                dialog.dismiss();

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("You have been disconnected from the server, You may have lost internet connection or the enemy player disconnected.")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Game.super.onBackPressed();
                            }
                        })
                        .create().show();


            }
                Log.e("Message", eventClass.message + " " +eventClass.fromClass);
        }

    }

    public void checkData() {
        if (points[0][0] != -1 && points[1][0] != -1) {
            EventBus.getDefault().post(new EventClass("ACTIVITY", color, points));
        }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(Game.this)
                .setTitle("Alert")
                .setMessage("Are you sure you want to forfeit")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Game.super.onBackPressed();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        })
                .create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(connection);
        Intent intent = new Intent(this, ServiceClass.class);
        stopService(intent);
        mBound = false;
    }
}
