package chess;

public class Rook extends ChessPiece {


    public Rook() {
        setPieceType(PieceType.ROOK);
    }



    @Override
    public boolean move(Chess chessboard, int finishRow, int finishCol) {
        //look for pieces nearby
        //if final position x or y is greater than nearby position, move is invalid
        //determine which way the piece is moving

        if(finishRow==getX()){
            //staying on same row
            if(finishCol>getY()){
                //moving right
                for(int i=getY()+1; i<=finishCol; i++){
                    if(chessboard.chessPieces[finishRow][i]!=null){
                        //found board piece
                        return false;
                    }else{
                        return true;
                    }
                }
            }else if(finishCol<getY()){
                //moving left
                for(int i=getY()-1; i>=finishCol; i--){
                    if(chessboard.chessPieces[finishCol][i]!=null){
                        //found board piece
                        return false;
                    }else{
                        return true;
                    }
                }
            }
        }else if(finishCol==getY()){
            //staying on same column
            if(finishRow>getX()){
                //moving forward
                for(int i=getX()+1; i<=finishRow; i++) {
                    if (chessboard.chessPieces[i][finishCol] != null) {
                        //found board piece
                        return false;
                    } else {
                        return true;
                    }
                }
            }else if(finishRow<getX()) {
                //moving moving backward
                for(int i=getX()-1; i>=finishRow; i--){
                    if(chessboard.chessPieces[i][finishCol]!=null){
                        //found board piece
                        return false;
                    }else{
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
