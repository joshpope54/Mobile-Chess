package chess;

public class Knight extends ChessPiece {
    public Knight() {
        setPieceType(PieceType.KNIGHT);
    }

    int[][] potentialMoves = {{2,1},{1,2},{-2,1},{-1,2},{-2,-1},{-1,-2},{2,-1},{1,-2}};

    @Override
    public boolean move(Chess chess, int finishRow, int finishCol) {

        boolean success = false;
        for(int[] possibleMove: potentialMoves){
            if(finishRow-getX()==possibleMove[0] && finishCol-getY()==possibleMove[1]){
                if(chess.chessPieces[finishRow][finishCol]==null){
                    movePieceInArray(chess, finishRow, finishCol);
                    success = true;
                }else{
                    success=false;
                }
            }
        }
        if (success){
            return true;
        }else{
            return false;
        }
    }
}
