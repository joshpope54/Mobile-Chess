package chess;

import static chess.ChessPiece.*;

public class Pawn extends ChessPiece {

    public Pawn() {
        setPieceType(PieceType.PAWN);
    }

    @Override
    public boolean move() {
        return false;
    }
}
