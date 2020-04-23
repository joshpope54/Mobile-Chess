package com.example.ce301.chess;

import sun.awt.windows.ThemeReader;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class King extends ChessPiece implements Serializable{
    ArrayList<ChessPiece> threateningPiece;

    public King() {
        threateningPiece = new ArrayList<>();
        setPieceType(PieceType.KING);
    }

    private int[][] potentialPositions = {{+1,0},{+1,-1},{0,-1},{-1,-1},{-1,0},{-1,+1},{0,+1},{+1,+1}};
    private boolean hasBeenCastled = false;

    @Override
    public Reason move(Chess chess, int finishRow, int finishCol, int type) {
        //check that finish row or finish col is 1 away
        //check if null
        boolean success = false;
        if(Math.abs(finishCol-getY())==2 && finishRow-getX()==0 && !hasBeenCastled) {
            if(finishCol-getY()==2){
                for(int i=getY()+1; i<=7; i++){
                    if(chess.chessPieces[getX()][i]==null){
                        continue;
                    }else if(chess.chessPieces[getX()][i].getPieceType() == PieceType.ROOK && i == 7){
                        hasBeenCastled = true;
                        chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                        chess.movePiece(getX(), i, finishRow, finishCol-1,false);
                        //((Rook)chess.chessPieces[][i]).movePieceInArray(chess, finishRow, finishCol-1);
                        return new Reason(true, "KING_SIDE_CASTLING");
                    }else{
                        success = false;
                        break;
                    }
                }
            }else if (finishCol-getY()==-2){
                for(int i=getY()-1; i>=0; i--){
                    if(chess.chessPieces[getX()][i]==null){
                        continue;
                    }else if(chess.chessPieces[getX()][i].getPieceType() == PieceType.ROOK && i == 0){
                        hasBeenCastled = true;
                        chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                        chess.movePiece(getX(), i, finishRow, finishCol+1,false);
                        //((Rook)chess.chessPieces[getX()][i]).movePieceInArray(chess, finishRow, finishCol+1);
                        return new Reason(true, "QUEEN_SIDE_CASTLING");
                    }else{
                        success = false;
                        break;
                    }
                }

            }
        }else{
            for (int[] possibleMove : potentialPositions) {
                if (finishRow - getX() == possibleMove[0] && finishCol - getY() == possibleMove[1]) {
                    if (chess.chessPieces[finishRow][finishCol] == null) {
                        //check if in check
                        //king is most complex piece
                        //must first check if king is in check
                        //move must then remove king from check
                        int startX = getX();
                        int startY = getY();
                        chess.movePiece(startX, startY, finishRow, finishCol,false);
                        if (this.checkIfInCheck(chess, true)) {
                            //replace with undo Move
                            //movePieceInArray(chess, startX, startY);
//                            chess.outputBoard();
//                            System.out.println("calling undo move from king move when position trying to move to is null");
//                            chess.undoMove();
//                            chess.outputBoard();
                            return new Reason(false, "CREATES_CHECK");
                        } else {
                            return new Reason(true, "SUCCESS");
                        }
                    } else {
                        //check if king is in check
                        if (!chess.chessPieces[finishRow][finishCol].getPieceColor().equals(this.getPieceColor())) {
                            chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                            if (this.checkIfInCheck(chess, true)) {
                                //replace with undo Move
                                //movePieceInArray(chess, startX, startY);
//                                System.out.println("calling undo move from king move when position trying to move to is not null and pieces not equal color");
//                                chess.undoMove();
                                return new Reason(false, "CREATES_CHECK");
                            } else {
                                return new Reason(true, "SUCCESS");
                            }
                        } else {
                            success = false;
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

    public boolean checkIfInCheck(Chess chess, Boolean record){
        //Check for any pieces attacking king
        //Check if king can move its self out of position
        //check for knight
        boolean knightCheck = ifTraceOnKnights(chess, record);
        boolean diag = ifTraceOnDiagonal(chess, record);
        boolean onX = ifTraceOnX(chess, record);
        boolean onY = ifTraceOnY(chess, record);
        return diag || onX || onY || knightCheck;
    }

    public Reason checkIfInCheckMate(Chess chess){
        //True if King Can Move
        //False if cant
        threateningPiece = new ArrayList<>();
        boolean inCheck = checkIfInCheck(chess, true);
        if(inCheck){
            boolean kingCanMove = checkIfKingCanMoveInCheck(chess);
            if(!kingCanMove){
                boolean pieceCanBlockOrTake = checkIfCheckingPieceCanBeBlockedOrTaken(chess); //true continue game //false end game
                if(pieceCanBlockOrTake){
                    return new Reason(true, "CHECK");
                }else{
                    return new Reason(true, "CHECKMATE");
                }
            }else{
                return new Reason(true, "CHECK");
            }

        }
        return new Reason(false, "NEITHER");
    }

    public boolean checkIfKingCanMoveInCheck(Chess chess){
        ArrayList<Boolean> noValidMovesChecker = new ArrayList<>();
        for (int[] move : potentialPositions) {
            int startX = this.getX();
            int startY = this.getY();
            int finishRow = startX+move[0];
            int finishCol = startY+move[1];

            if(finishRow>7 ||finishRow<0 ||  finishCol>7 || finishCol<0 ) {
                noValidMovesChecker.add(true);
            }else{
                //System.out.println(startX + "   " + startY + "   " + finishRow + "   " + finishCol);
                boolean success = chess.movePiece(startX, startY, finishRow, finishCol,false);
                if(this.checkIfInCheck(chess, false) && success){
                    //System.out.println("Calling undo move from check if king can move in check when move places king in check" + this.getPieceColor());
                    chess.undoMove();
                    noValidMovesChecker.add(true);
                }else if(!this.checkIfInCheck(chess, false) && success){
                    //replace with undoMove
                    //System.out.println("Calling undo move from check if king can move in check when move doesnt place king in check" + this.getPieceColor());
                    chess.undoMove();
                    noValidMovesChecker.add(false);
                }
            }
        }
        return noValidMovesChecker.contains(false);
    }

    public boolean checkIfCheckingPieceCanBeBlockedOrTaken(Chess chess){
        HashMap<int[], Boolean> availableMovementSpots = new HashMap<>();
        for(ChessPiece piece: threateningPiece){
            if(piece.getPieceType().equals(PieceType.KNIGHT)){
                int[][] potentialPositions = {{2,1},{1,2},{-2,1},{-1,2},{-2,-1},{-1,-2},{2,-1},{1,-2}};
                for(int[] positionOfRook: potentialPositions){
                    if(piece.getX() == this.getX()+positionOfRook[0] && this.getY()+positionOfRook[1]==piece.getY()){
                        availableMovementSpots.put(new int[]{piece.getX(), piece.getY()}, false);
                    }
                }
            }else{
                if(piece.getY()== this.getY()){            //Piece on Vertical
                    if(piece.getX()>this.getX()){
                        for(int i=getX()+1; i<=piece.getX();i++) {
                            availableMovementSpots.put(new int[]{i, getY()}, false); //False if no piece can move to this spot //True if move can be made
                        }
                    }else{
                        for(int i=getX()-1; i>=piece.getX();i--) {
                            availableMovementSpots.put(new int[]{i, getY()}, false);
                        }
                    }
                }else if (piece.getX() == this.getX()){                //Piece on Horizontal
                    if(piece.getY()>this.getY()){
                        for(int j=getY()+1; j<=piece.getY();j++) {
                            availableMovementSpots.put(new int[]{getX(), j}, false);
                        }
                    }else{
                        for(int j=getY()-1; j>=piece.getY();j--) {
                            availableMovementSpots.put(new int[]{getX(), j}, false);
                        }
                    }
                }else if(piece.getX() < this.getX() && piece.getY()< this.getY()){
                    //System.out.println("Up and left");
                    for(int i=getX()-1, j=getY()-1; (i>=piece.getX() && j>=piece.getY()); i--, j--) {
                        availableMovementSpots.put(new int[]{i, j}, false);
                    }
                }else if (piece.getX() < this.getX() && piece.getY() > this.getY()){
                    //System.out.println("Up and right");
                    for(int i=getX()-1, j=getY()+1; (i>=piece.getX() && j<=piece.getY()); i--, j++) {
                        availableMovementSpots.put(new int[]{i, j}, false);
                    }
                }else if (piece.getX() > this.getX() && piece.getY() > this.getY()){
                    //System.out.println("down and right");
                    for(int i=getX()+1, j=getY()+1; (i<=piece.getX() && j<=piece.getY()); i++, j++) {
                        availableMovementSpots.put(new int[]{i, j}, false);
                    }
                }else if (piece.getX() > this.getX() && piece.getY() < this.getY()) {
                    //System.out.println("down and left");
                    for (int i = getX() + 1, j = getY() - 1; (i <= piece.getX() && j >= piece.getY()); i++, j--) {
                        availableMovementSpots.put(new int[]{i, j}, false);
                    }
                }
            }
        }

        //System.out.println("KING COLOR: "+ this.getPieceColor() + threateningPiece.toString());
        for(int[] piecePos: availableMovementSpots.keySet()){
            if(this.getPieceColor().equals(PieceColor.WHITE)){
                for(ChessPiece piece : chess.aliveWhitePieces){
                    if(!(piece instanceof King)){
                        if(piece.move(chess, piecePos[0],piecePos[1], 1).isSuccess()){
                            //System.out.println("PROBLEM AREA: " + piecePos[0] +"  " + piecePos[1] + piece);
                            availableMovementSpots.replace(piecePos,true);
                        }
                    }
                }
            }else if (this.getPieceColor().equals(PieceColor.BLACK)){
                for(ChessPiece piece : chess.aliveBlackPieces){
                    if(!(piece instanceof King)) {
                        if (piece.move(chess, piecePos[0], piecePos[1], 1).isSuccess()) {
                            availableMovementSpots.replace(piecePos, true);
                        }
                    }
                }
            }

        }
//        for(int[] piecePos: availableMovementSpots.keySet()) {
//            System.out.println(Arrays.toString(piecePos) + "  " + availableMovementSpots.get(piecePos));
//        }
        for(int[] piecePos: availableMovementSpots.keySet()){
            if(availableMovementSpots.get(piecePos)){
                return true;
            }
        }
        return false;
    }

    private String checkPieces(Chess chess, int i, int j){
        if(chess.chessPieces[i][j] != null){
            if(chess.chessPieces[i][j].getPieceColor().equals(this.getPieceColor())){
                return "OWNPIECE ";
            }else{
                if(chess.chessPieces[i][j] instanceof Queen){
                    return "ENEMYPIECE QUEEN";
                }else if(chess.chessPieces[i][j] instanceof Bishop){
                    return "ENEMYPIECE BISHOP";
                }else if(chess.chessPieces[i][j] instanceof Knight){
                    return "ENEMYPIECE KNIGHT";
                }else if(chess.chessPieces[i][j] instanceof Pawn){
                    return "ENEMYPIECE PAWN";
                }
            }
        }
        return "";
    }

    private boolean ifTraceOnKnights(Chess chess, Boolean record){
        int[][] potentialMoves = {{2,1},{1,2},{-2,1},{-1,2},{-2,-1},{-1,-2},{2,-1},{1,-2}};
        for (int[] move : potentialMoves){
            try {
                if(chess.chessPieces[getX()+move[0]][getY()+move[1]]!=null){
                    if(!chess.chessPieces[getX()+move[0]][getY()+move[1]].getPieceColor().equals(this.getPieceColor())){
                        if(chess.chessPieces[getX()+move[0]][getY()+move[1]] instanceof Knight){
                            if(!threateningPiece.contains(chess.chessPieces[getX()+move[0]][getY()+move[1]]) && record){
                                //System.out.println("FOUND THREATENING KNIGHT " + chess.chessPieces[getX()+move[0]][getY()+move[1]]);
                                threateningPiece.add(chess.chessPieces[getX()+move[0]][getY()+move[1]]);
                            }
                            return true;
                        }
                    }
                }
            }catch (ArrayIndexOutOfBoundsException ignored){
            }
        }
        return false;
    }

    private boolean ifTraceOnDiagonal(Chess chess, Boolean record){
        String successFrom1 = "";
        String successFrom2 = "";
        String successFrom3 = "";
        String successFrom4 = "";

        boolean successBoolean1 = false;
        boolean successBoolean2 = false;
        boolean successBoolean3 = false;
        boolean successBoolean4 = false;
        //Down and Right
        for(int i=getX()+1, j=getY()+1; (i<=7 && j<=7); i++, j++) {
            successFrom1 = checkPieces(chess, i, j);
            String[] successArray = successFrom1.split(" ");
            if(i==7 || j==7){
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                    threateningPiece.add(chess.chessPieces[i][j]);
                                }
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                threateningPiece.add(chess.chessPieces[i][j]);
                            }
                            successBoolean1=true;
                        }
                    }
                    break;
                }
                break;
            }else{
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                    threateningPiece.add(chess.chessPieces[i][j]);
                                }
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                threateningPiece.add(chess.chessPieces[i][j]);
                            }
                            successBoolean1=true;
                        }
                    }
                    break;
                }
            }
        }

        for(int i=getX()-1, j=getY()-1; (i>=0 && j>=0); i--, j--) {
            successFrom2 = checkPieces(chess, i, j);
            String[] successArray = successFrom2.split(" ");
            if(i==0 || j==0){
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                    threateningPiece.add(chess.chessPieces[i][j]);
                                }
                                successBoolean2=true;
                            }else{
                                successBoolean2=false;
                            }
                        }else{
                            if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                threateningPiece.add(chess.chessPieces[i][j]);
                            }
                            successBoolean2=true;
                        }
                    }
                    break;
                }
                break;
            }else{
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                    threateningPiece.add(chess.chessPieces[i][j]);
                                }
                                successBoolean2=true;
                            }else{
                                successBoolean2=false;
                            }
                        }else{
                            if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                threateningPiece.add(chess.chessPieces[i][j]);
                            }
                            successBoolean2=true;
                        }
                    }
                    break;
                }
            }
        }
        for(int i=getX()-1, j=getY()+1; (i>=0 && j<=7); i--, j++) {
            successFrom3 = checkPieces(chess, i, j);
            String[] successArray = successFrom3.split(" ");
            if(i==0 || j==7){
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                    threateningPiece.add(chess.chessPieces[i][j]);
                                }
                                successBoolean3=true;
                            }else{
                                successBoolean3=false;
                            }
                        }else{
                            if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                threateningPiece.add(chess.chessPieces[i][j]);
                            }
                            successBoolean3=true;
                        }
                    }
                    break;
                }
                break;
            }else{
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                    threateningPiece.add(chess.chessPieces[i][j]);
                                }
                                successBoolean3=true;
                            }else{
                                successBoolean3=false;
                            }
                        }else{
                            if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                threateningPiece.add(chess.chessPieces[i][j]);
                            }
                            successBoolean3=true;
                        }
                    }
                    break;
                }
            }
        }
        for(int i=getX()+1, j=getY()-1; (i<=7 && j>=0); i++, j--) {
            successFrom4 = checkPieces(chess, i, j);
            String[] successArray = successFrom4.split(" ");

            if(i==7 || j==0){
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                    threateningPiece.add(chess.chessPieces[i][j]);
                                }
                                successBoolean4=true;
                            }else{
                                successBoolean4=false;
                            }
                        }else{
                            if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                threateningPiece.add(chess.chessPieces[i][j]);
                            }
                            successBoolean4=true;
                        }
                    }
                    break;
                }
                break;
            }else{
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                    threateningPiece.add(chess.chessPieces[i][j]);
                                }
                                successBoolean4=true;
                            }else{
                                successBoolean4=false;
                            }
                        }else{
                            if(!threateningPiece.contains(chess.chessPieces[i][j]) && record) {
                                threateningPiece.add(chess.chessPieces[i][j]);
                            }
                            successBoolean4=true;
                        }
                    }
                    break;
                }
            }
        }
        return successBoolean1 || successBoolean2 || successBoolean3 || successBoolean4;
    }

    @SuppressWarnings("Duplicates")
    public boolean ifTraceOnY(Chess chess, Boolean record){
        boolean success = false;
        for(int i=getX()-1; i>=0;i--){
            if(chess.chessPieces[i][getY()] != null){
                if(chess.chessPieces[i][getY()].getPieceColor().equals(this.getPieceColor())){
                    success = false;
                    break;
                }else{
                    if(chess.chessPieces[i][getY()] instanceof Queen){
                        if(!threateningPiece.contains(chess.chessPieces[i][getY()]) && record) {
                            threateningPiece.add(chess.chessPieces[i][getY()]);
                        }
                        success = true;
                        break;
                    }else if(chess.chessPieces[i][getY()] instanceof Rook){
                        if(!threateningPiece.contains(chess.chessPieces[i][getY()]) && record) {
                            threateningPiece.add(chess.chessPieces[i][getY()]);
                        }
                        success = true;
                        break;
                    }else{
                        success = false;
                        break;
                    }
                }
            }
        }
        for(int i=getX()+1; i<=7;i++){
            if(chess.chessPieces[i][this.getY()] != null){
                if(chess.chessPieces[i][this.getY()].getPieceColor().equals(this.getPieceColor())){
                    success = false;
                    break;
                }else{
                    if(chess.chessPieces[i][this.getY()] instanceof Queen){
                        if(!threateningPiece.contains(chess.chessPieces[i][this.getY()]) && record) {
                            threateningPiece.add(chess.chessPieces[i][this.getY()]);
                        }
                        success = true;
                        break;
                    }else if(chess.chessPieces[i][this.getY()] instanceof Rook){
                        if(!threateningPiece.contains(chess.chessPieces[i][this.getY()]) && record) {
                            threateningPiece.add(chess.chessPieces[i][this.getY()]);
                        }
                        success = true;
                        break;
                    }else{
                        success = false;
                        break;
                    }
                }
            }
        }
        return success;
    }

    @SuppressWarnings("Duplicates")
    private boolean ifTraceOnX(Chess chess, Boolean record){
        boolean success = false;
        for(int i=getY()-1; i>=0;i--){
            if(chess.chessPieces[getX()][i] != null){
                if(chess.chessPieces[getX()][i].getPieceColor().equals(this.getPieceColor())){
                    success = false;
                    break;
                }else{
                    if(chess.chessPieces[getX()][i] instanceof Queen){
                        if(!threateningPiece.contains(chess.chessPieces[getX()][i]) && record) {
                            threateningPiece.add(chess.chessPieces[getX()][i]);
                        }
                        success = true;
                        break;
                    }else if(chess.chessPieces[getX()][i] instanceof Rook){
                        if(!threateningPiece.contains(chess.chessPieces[getX()][i]) && record) {
                            threateningPiece.add(chess.chessPieces[getX()][i]);
                        }
                        success = true;
                        break;
                    }else{
                        success = false;
                        break;
                    }
                }
            }
        }
        for(int i=getY()+1; i<=7;i++){
            if(chess.chessPieces[getX()][i] != null){
                if(chess.chessPieces[getX()][i].getPieceColor().equals(this.getPieceColor())){
                    success = false;
                    break;
                }else{
                    if(chess.chessPieces[getX()][i] instanceof Queen){
                        if(!threateningPiece.contains(chess.chessPieces[getX()][i]) && record) {
                            threateningPiece.add(chess.chessPieces[getX()][i]);
                        }
                        success = true;
                        break;
                    }else if(chess.chessPieces[getX()][i] instanceof Rook){
                        if(!threateningPiece.contains(chess.chessPieces[getX()][i]) && record) {
                            threateningPiece.add(chess.chessPieces[getX()][i]);
                        }
                        success = true;
                        break;

                    }else{
                        success = false;
                        break;
                    }
                }
            }
        }
        return success;
    }
}
