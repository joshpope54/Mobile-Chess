package com.example.ce301.chess;

import java.io.Serializable;

public class Rook extends ChessPiece implements Serializable {


    public Rook() {
        setPieceType(PieceType.ROOK);
    }



    @Override
    public Reason move(Chess chess, int finishRow, int finishCol, int type) {
        //look for pieces nearby
        //if final position x or y is greater than nearby position, move is invalid
        //determine which way the piece is moving
        //When do i take? a piece?
        boolean success = false;

        if(finishRow==getX()){
            //staying on same row
            if(finishCol>getY()){
                //moving right
                for(int i=getY()+1; i<=finishCol; i++){
                    if(chess.chessPieces[finishRow][i] == null){
                        if(i!=finishCol){
                            continue;
                        }else{
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol);
                            }
                            success=true;
                        }
                    }else{
                        if(i!=finishCol){
                            break;
                        }else{
                            if(this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                success=false;
                            }else{
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        }
                    }
                }
            }else if(finishCol<getY()){
                //moving left
                for(int i=getY()-1; i>=finishCol; i--){
                    if(chess.chessPieces[finishRow][i] == null){
                        if(i!=finishCol){
                            continue;
                        }else{
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol);
                            }
                            success=true;
                        }
                    }else{
                        if(i!=finishCol){
                            break;
                        }else{
                            if(this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                success=false;
                            }else{
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        }
                    }
                }
            }
        }else if(finishCol==getY()){
            //staying on same column
            if(finishRow>getX()){
                //moving forward
                for(int i=getX()+1; i<=finishRow; i++) {
                    if(chess.chessPieces[i][finishCol] == null){
                        if(i!=finishRow){
                            continue;
                        }else{
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol);
                            }
                            success=true;
                        }
                    }else{
                        if(i!=finishRow){
                            break;
                        }else{
                            if(this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                success=false;
                            }else{
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        }
                    }
                }
            }else if(finishRow<getX()) {
                //moving moving backward
                for(int i=getX()-1; i>=finishRow; i--){
                    if(chess.chessPieces[i][finishCol] == null){
                        if(i!=finishRow){
                            continue;
                        }else{
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol);
                            }
                            success=true;
                        }
                    }else{
                        if(i!=finishRow){
                            break;
                        }else{
                            if(this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                success=false;
                            }else{
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        }
                    }
                }
            }
        }

        if(success){
            return new Reason(true, "SUCCESS");
        }else{
            return new Reason(false, "FAILURE");
        }
    }
}


//    public ArrayList<int[]> findCheckingPiece(){
//        //loop from king in all direction until encountered a enemy piece
//        int kingSelectionNumber = findKingByName();
//        int kingX = -1;
//        int kingY = -1;
//        ArrayList<int[]> points = new ArrayList<>(); //HOLDS THE POINTS ON WHICH A MOVE MUST BE MADE, OR KING MOVED
//        for (int i = 0; i < gridConverter.size(); i++) {
//            if (gridConverter.get(i).contains(kingSelectionNumber)) {
//                int gridXPosition = i;
//                int gridYPosition = gridConverter.get(i).indexOf(kingSelectionNumber);
//                kingX = gridXPosition;
//                kingY = gridYPosition;
//            }
//        }
//        if(kingX!=-1 && kingY!=-1) {
//            int[][] potentialPositions = {{+1, 0}, {+1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, +1}, {0, +1}, {+1, +1}};
//            int count = 0;
//            ArrayList<Integer> directionPieceWasFound = new ArrayList<>();
//            for(int[] position: potentialPositions){
//                for(int j = kingX+position[0], k = kingY+position[1]; ((k >= 0 && k<=7) && (j >= 0 && j<=7)); j+=position[0], k+=position[1]){
//                    //System.out.println("POSITION" + count + "  j:" + j + "  k:"+k);
//                    int gridLayoutPosition = gridConverter.get(j).get(k);
//                    RelativeLayout layout = (RelativeLayout)gridLayout.getChildAt(gridLayoutPosition);
//                    TextView text = (TextView)layout.getChildAt(2);
//                    if(!text.getText().toString().equals("")){
//
//                        if(text.getText().toString().charAt(0) != color.charAt(0)){
//                            if(text.getText().toString().contains("PAWN")){
//                                break;
//                            }else{
//                                directionPieceWasFound.add(count);
//                                break;
//                            }
//                        }else{
//                            break;
//                        }
//                    }
//                }
//                count++;
//            }
//
//            for(int i=1; i<=7;i+=2){
//                for(int j = kingX+potentialPositions[i][0], k = kingY+potentialPositions[i][1]; ((k >= 0 && k<=7) && (j >= 0 && j<=7)); j+=potentialPositions[i][0], k+=potentialPositions[i][1]){
//                    //System.out.println("POSITION" + count + "  j:" + j + "  k:"+k);
//                    int gridLayoutPosition = gridConverter.get(j).get(k);
//                    RelativeLayout layout = (RelativeLayout)gridLayout.getChildAt(gridLayoutPosition);
//                    TextView text = (TextView)layout.getChildAt(2);
//                    if(!text.getText().toString().equals("")){
//                        if(text.getText().toString().charAt(0) != color.charAt(0)){
//                            if(text.getText().toString().contains("PAWN")){
//                                directionPieceWasFound.add(i);
//                                break;
//                            }else{
//                                break;
//                            }
//                        }else{
//                            break;
//                        }
//                    }
//                }
//            }
//
//            for(Integer inte: directionPieceWasFound){
//                for(int j = kingX+potentialPositions[inte][0], k = kingY+potentialPositions[inte][1]; ((k >= 0 && k<=7) && (j >= 0 && j<=7)); j+=potentialPositions[inte][0], k+=potentialPositions[inte][1]){
//                    int gridLayoutPosition = gridConverter.get(j).get(k);
//                    RelativeLayout layout = (RelativeLayout)gridLayout.getChildAt(gridLayoutPosition);
//                    TextView text = (TextView)layout.getChildAt(2);
//                    if(!text.getText().toString().equals("")) {
//                        if (text.getText().toString().charAt(0) != color.charAt(0)) {
//                            int [] array = new int[2];
//                            array[0] = j;
//                            array[1] = k;
//                            points.add(array);
//                        }
//                        break;
//                    }else{
//                        int [] array = new int[2];
//                        array[0] = j;
//                        array[1] = k;
//                        points.add(array);
//                    }
//
//                }
//            }
//        }
////        for(int[] array: points){
////            System.out.println(Arrays.toString(array));
////        }
//        return points;
//    }