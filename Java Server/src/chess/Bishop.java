package chess;

public class Bishop extends ChessPiece {
    public Bishop() {
        setPieceType(PieceType.BISHOP);
    }

    @Override
    public boolean move() {
        return false;
    }
}
