package chess;

public class Rook extends ChessPiece {


    public Rook() {
        setPieceType(PieceType.ROOK);
    }

    @Override
    public boolean move(Chess chessboard, int row, int col) {

        //look for pieces nearby
        //if final position x or y is greater than nearby position, move is invalid
        //

        //determine which way the piece is moving
        //

        if(row==getY()){
            //staying on same row
            if(col>getY()){
                //moving right
                System.out.println("RIGHT");
            }else if(col<getY()){
                //moving left
                System.out.println("LEFT ");

            }

        }else if(col==getX()){
            //staying on same column


            if(row>getX()){
                //moving forward
                System.out.println("FORWARD");

            }else if(row<getX()) {
                //moving moving backward
                System.out.println("BACKWARD");

            }
        }




        return false;
    }
}
