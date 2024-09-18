package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        //return piece color
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

//    public boolean inBoundsColumn(ChessPosition myPosition) {
//        if(myPosition.getColumn() - 1 < 0 || myPosition.getColumn() + 1 > 8){
//            return false;
//        }
//        return true;
//    }
//    public boolean inBoundsRow(ChessPosition myPosition) {
//        if(myPosition.getRow() - 1 < 0 || myPosition.getRow() + 1 > 8) {
//            return false;
//        }
//        return true;
//    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        /* Thoughts before coding: only showing valid moves,
        don't worry about if other pieces are in the way for now
        also make a good helper function to make sure your pieces are in bounds*/
        Collection<ChessMove>list_moves = new ArrayList<>();

        if(this.pieceType == PieceType.PAWN){
            /*if pawn has not moved yet, can move up one or 2 squares
            * else pawn moves up only one
            * if there is a piece diagonal that is not the same color, can move to that spot
            * after a move is made, delete the piece and addPiece of the same value. So it creates itself before deleting itself.
            * */
            System.out.println("Hello, PAWN!");
        }

        //how king moves
        else if(this.pieceType == PieceType.KING) {
            //rules valid rules for the king
//            if(inBoundsRow() == true) {
//
//            }

            System.out.println("Hello, KING!");
        }
        //how knight moves
        else if(this.pieceType == PieceType.KNIGHT) {
            System.out.println("Hello, KNIGHT!");
        }
        //how bishop moves
        else if(this.pieceType == PieceType.BISHOP) {
//            System.out.println("Hello, BISHOP!");
            //moves diagonal so +1 to the row and +1 or -1 for column if its moving up, if moving down its -1 row and -1 and +1 col
            boolean left_up = true;
            boolean right_up = true;
            boolean down_left = true;
            boolean down_right = true;

            ChessPosition new_position = myPosition;
            ChessMove move;

            //diagonal down left and right
            while(down_left == true){
                int new_row = new_position.getRow() - 1;
                int col_left = new_position.getColumn() - 1;

                //base case if it goes too far left or too far down
                if(new_row < 1 || col_left < 1) {
                    new_position = myPosition;
                    break;
                }


                //new chess piece position
                ChessPosition up_position = new ChessPosition(new_row, col_left);

                //new chess move
                ChessMove moves = new ChessMove(myPosition, up_position,null);

                //add if null or empty square
                if (board.getPiece(up_position) == null) {
                    list_moves.add(moves);
                }

                //if null do two team color stuff
                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    break;
                }
                //add possibe move and then break
                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    break;
                }


                //update new or copy to the left
                new_position = up_position;
            }

            while(down_right == true){
                int new_row = new_position.getRow() - 1;
                int col_right = new_position.getColumn() + 1;

                //base case if it goes too far right or too far down
                if(new_row < 1 || col_right > 8) {
                    //reset position
                    new_position = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition up_position = new ChessPosition(new_row, col_right);

                //new moves
                ChessMove moves = new ChessMove(myPosition, up_position,null);

                //add if null or empty square
                if (board.getPiece(up_position) == null) {
                    list_moves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    break;
                }


                //add possibe move and then break
                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    list_moves.add(moves);
                    break;
                }
                //update new or copy to the left
                new_position = up_position;
            }


            //diagonal up left and right
            while(left_up == true){
                int new_row = new_position.getRow() + 1;
                int col_left = new_position.getColumn() - 1;
                //int col_right = new_position.getColumn() + 1;

                //base case if it goes too far left or too high up
                if(new_row > 8 || col_left < 1) {
                    new_position = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition up_position = new ChessPosition(new_row, col_left);

                //new moves
                ChessMove moves = new ChessMove(myPosition, up_position,null);


                //add if null or empty square
                if (board.getPiece(up_position) == null) {
                    list_moves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    break;
                }
                //add if null or empty square
                else if (board.getPiece(up_position) == null) {
                    list_moves.add(moves);
                }
                //add possibe move and then break
                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    break;
                }
                //update new or copy to the left
                new_position = up_position;
            }
            while(right_up == true){
                int new_row = new_position.getRow() + 1;
//                int col_left = new_position.getColumn() - 1;
                int col_right = new_position.getColumn() + 1;

                //base case if it goes to far right or up
                if(new_row > 8 || col_right > 8) {
                    new_position = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition up_position = new ChessPosition(new_row, col_right);

                //new moves
                ChessMove moves = new ChessMove(myPosition, up_position,null);

                //add if null or empty square
                if (board.getPiece(up_position) == null) {
                    list_moves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    break;
                }

                //add possibe move and then break
                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    break;
                }
                //update new or copy to the left
                new_position = up_position;
            }

        }

        //how rook moves
        else if(this.pieceType == PieceType.ROOK){
            boolean left = true;
            boolean right = true;
            boolean up = true;
            boolean down = true;
            ChessPosition new_position = myPosition;
            //find out where it is on the board, go to the left, up, right, down and return a list of array.

            //GOING LEFT
            while(left == true) {
                int new_col = new_position.getColumn() - 1;

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(new_col < 1) {
                    new_position = myPosition;
                    break;
                }

                //new chess position
                ChessPosition left_position = new ChessPosition(myPosition.getRow(), new_col);
                //new chess move
                ChessMove moves = new ChessMove(myPosition, left_position,null);

                if (board.getPiece(left_position) == null) {
                    list_moves.add(moves);
                }
                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                else if (board.getPiece(left_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    break;
                }
                else if(board.getPiece(left_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    break;
                }
                //update new or copy to the left
                new_position = left_position;
            }


            //GOING RIGHT
            while(right == true) {
                int new_col = new_position.getColumn() + 1;
                ChessPosition left_position = new ChessPosition(myPosition.getRow(), new_col);

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(new_col > 8) {
                    new_position = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, left_position,null);

                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(left_position) == null) {
                    list_moves.add(moves);
                }
                else if (board.getPiece(left_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    break;
                }
                else if(board.getPiece(left_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    break;
                }
                //update new or copy to the left
                new_position = left_position;
            }

            // GOING UP

            while(up == true){
                int new_row = new_position.getRow() + 1;
                ChessPosition up_position = new ChessPosition(new_row, myPosition.getColumn());

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(new_row > 8) {
                    new_position = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, up_position,null);



                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(up_position) == null) {
                    list_moves.add(moves);
                }
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    break;
                }

                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    break;
                }
                //update new or copy to the left
                new_position = up_position;
            }

            //GOING DOWN

            while(down == true){
                int new_row = new_position.getRow() - 1;
                ChessPosition up_position = new ChessPosition(new_row, myPosition.getColumn());

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(new_row < 1) {
                    new_position = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, up_position,null);

                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(up_position) == null) {
                    list_moves.add(moves);
                }
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    break;
                }

                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    break;
                }
                //update new or copy to the left
                new_position = up_position;
            }

            //(myPosition.getRow())(myPosition.getColumn())
        }

        //how queen moves
        else if(this.pieceType == PieceType.QUEEN){
            System.out.println("Hello, QUEEN!");
        }
        return list_moves;
        //return new ArrayList<>();
    }
}
