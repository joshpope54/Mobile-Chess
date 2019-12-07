package chess;

import static chess.ChessPiece.*;

public class Pawn extends ChessPiece {



    public Pawn() {
        setPieceType(PieceType.PAWN);
    }

    @Override
    public boolean move(Chess chess, int finishRow, int finishCol) {
        //get chess board instance
        //System.out.println(chess.chessPieces[this.getX()][this.getY()]);


        //check if piece is able to be moved to position
        //pawn
        //1 forward
        //2 forward if in first position
        //diagonal if can take piece

        //Question ->
        //How to check diagonal?
        //perhaps valid moves array?
        //First check if in default position to allow movement of 2 forward

        //check if moving backwards
        //cant allow that

        if(getPieceColor().equals(PieceColor.WHITE)){
            if(getX()-finishRow==-1){
                return false;
            }else{
                if(getX()==1 || getX()==6 ){
                    Boolean x = getaBoolean(chess, finishRow, finishCol);
                    if (x != null) return x;
                }else{
                    Boolean x = getaBoolean(chess, finishRow, finishCol);
                    if (x != null) return x;
                }
            }
        }else{
            if(finishRow-getX()==-1){
                return false;
            }else{
                if(getX()==1 || getX()==6 ){
                    Boolean x = getaBoolean(chess, finishRow, finishCol);
                    if (x != null) return x;
                }else{
                    Boolean x = getaBoolean(chess, finishRow, finishCol);
                    if (x != null) return x;
                }
            }
        }

        return false;
    }

    private Boolean getaBoolean(Chess chess, int finishRow, int finishCol) {
        if(getX()+1==finishRow && getY()+1==finishCol){
            if(chess.chessPieces[finishRow][finishCol]!=null){
                if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                    movePieceInArray(chess, finishRow, finishCol);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if(finishCol!=getY()){
                return false;
            }else{
                if(finishRow-getX()==2||finishRow-getX()==1){
                    if(chess.chessPieces[finishRow][finishCol]==null){
                        movePieceInArray(chess, finishRow, finishCol);
                        return true;
                    }else{
                        System.out.println("not allowed");
                        return false;
                    }
                }else{
                    return false;
                }
            }
        }
    }



}
