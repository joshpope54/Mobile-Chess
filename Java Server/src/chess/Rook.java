package chess;

public class Rook extends ChessPiece {


    public Rook() {
        setPieceType(PieceType.ROOK);
    }

    @Override
    public boolean move() {
        return false;
    }
}
