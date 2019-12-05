package chess;

public class Knight extends ChessPiece {
    public Knight() {
        setPieceType(PieceType.KNIGHT);
    }

    @Override
    public boolean move(Chess chessboard, int row, int col) {
        return false;
    }
}
