package chess;

import static chess.ChessPiece.*;

public class Pawn extends ChessPiece {

    public Pawn() {
        setPieceType(PieceType.PAWN);
    }

    @Override
    public boolean move(Chess chess, int row, int col) {
        //get chess board instance
        System.out.println(chess.chessPieces[this.getX()][this.getY()]);


        //check if piece is able to be moved to position
        //pawn
        //1 forward
        //2 forward if in first position
        //diagonal if can take piece




        return false;
    }

}
