package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        //if my address matches default equals
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //make a new object called THAT; CASTING
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */



    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        /* Thoughts before coding: only showing valid moves,
        don't worry about if other pieces are in the way for now
        also make a good helper function to make sure your pieces are in bounds*/
        Collection<ChessMove>list_moves = new ArrayList<>();

        if(this.pieceType == PieceType.PAWN){
            /* if and only if move diagonal if opposite color is on those sides
            * movement is row+1 always and depending on if there are pieces diagonally col +1 or -1
            *  */
            int new_row = myPosition.getRow();
            int new_col = myPosition.getColumn();
            boolean bool_pawn_white = false;
            if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
                bool_pawn_white = true;
            }


            if(bool_pawn_white == true) {
                //check to see if pawn has moved or not

                //pawn rules of movement
                for (int i = 0; i < 2; i++) {
                    if (new_row > 8 || new_col < 1 || new_col > 8) {
                        continue;
                    }
                    //new chess piece position it can maybe go to
                    if (i == 0) {
                        int base_Col = new_col - 1;
                        ChessPosition left_up_position = new ChessPosition(new_row + 1, base_Col - 1);
                        if (new_row > 8 || base_Col < 1 || base_Col > 8) {
                            continue;
                        }
                        ChessMove basecase_move = new ChessMove(myPosition, left_up_position, null);
                        if (board.getPiece(left_up_position) == null ||
                                (board.getPiece(left_up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor())) {
                            continue;
                        } else if (board.getPiece(left_up_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            list_moves.add(basecase_move);
                            continue;
                        }
                    }
                    ChessPosition new_position = new ChessPosition(new_row + 1, new_col + (i - 1));
                    //new move
                    ChessMove moves = new ChessMove(myPosition, new_position, null);
                    if (board.getPiece(new_position) == null) {
                        list_moves.add(moves);
                        new_position = myPosition;
                        continue;
                    }
                    // If the square has a piece of the same color, stop further checks in this direction
                    else if (board.getPiece(new_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        new_position = myPosition;
                        continue;
                    } else if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        if (i==1) {
                            continue;
                        }
                        list_moves.add(moves);
                        new_position = myPosition;
                        continue;
                    }

                }
            }
            //else if pawn is black
            else {
                //check to see if pawn has moved or not

                //pawn rules of movement
                for (int i = 0; i < 2; i++) {
                    if (new_row < 1 || new_col < 1 || new_col > 8) {
                        continue;
                    }
                    //new chess piece position it can maybe go to
                    if (i == 0) {
                        int base_Col = new_col - 1;
                        ChessPosition left_down_position = new ChessPosition(new_row-1, base_Col - 1);
                        if (new_row < 1 || base_Col < 1 || base_Col > 8) {
                            continue;
                        }
                        ChessMove basecase_move = new ChessMove(myPosition, left_down_position, null);
                        if (board.getPiece(left_down_position) == null ||
                                (board.getPiece(left_down_position).getTeamColor() == board.getPiece(myPosition).getTeamColor())) {
                            continue;
                        } else if (board.getPiece(left_down_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            list_moves.add(basecase_move);
                            continue;
                        }
                    }
                    ChessPosition new_position = new ChessPosition(new_row - 1, new_col + (i - 1));
                    //new move
                    ChessMove moves = new ChessMove(myPosition, new_position, null);
                    if (board.getPiece(new_position) == null) {
                        list_moves.add(moves);
                        new_position = myPosition;
                        continue;
                    }
                    // If the square has a piece of the same color, stop further checks in this direction
                    else if (board.getPiece(new_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        new_position = myPosition;
                        continue;
                    }
                    else if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        //if pawn is trying to go vertical color doesn't matter, skip
                        if (i==1) {
                            continue;
                        }
                        list_moves.add(moves);
                        new_position = myPosition;
                        continue;
                    }

                }
            }
        }

        //how king moves
        else if(this.pieceType == PieceType.KING) {
            // Directions for the king: (row delta, column delta)
            int[][] directions = {
                    {-1, 0},  // down
                    {1, 0},   // up
                    {0, -1},  // left
                    {0, 1},   // right
                    {-1, -1}, // down-left
                    {-1, 1},  // down-right
                    {1, -1},  // up-left
                    {1, 1}    // up-right
            };

            for (int[] direction : directions) {
                int new_row = myPosition.getRow() + direction[0];
                int new_col = myPosition.getColumn() + direction[1];

                // Base case: Check if the position is out of bounds
                if (new_row < 1 || new_row > 8 || new_col < 1 || new_col > 8) {
                    continue;
                }

                ChessPosition new_position = new ChessPosition(new_row, new_col);
                ChessMove move = new ChessMove(myPosition, new_position, null);

                // If the square is empty, add the move and stop further checks in this direction
                if (board.getPiece(new_position) == null) {
                    list_moves.add(move);
                }
                // If the square has a piece of the same color, stop further checks in this direction
                else if (board.getPiece(new_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    continue;
                }
                // If the square has an opponent's piece, add the move and stop further checks in this direction
                else if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    list_moves.add(move);
                }
            }


        }


        //how knight moves
        else if(this.pieceType == PieceType.KNIGHT) {
            boolean left_up = true;
            boolean right_up = true;
            boolean down_left = true;
            boolean down_right = true;

            ChessPosition new_position = myPosition;
            ChessMove move;
            int j = 1;

            //upleft
            for (int i = 2; i > 0; i--){

                int new_row = new_position.getRow() + j;
                int col_left = new_position.getColumn() - i;
                //int col_right = new_position.getColumn() + 1;

                //base case if it goes too far left or too high up
                if(new_row > 8 || col_left < 1) {
                    j++;
                    new_position = myPosition;
                    continue;
                }

                //new chess piece position it can maybe go to
                ChessPosition up_position = new ChessPosition(new_row, col_left);

                //new moves
                ChessMove moves = new ChessMove(myPosition, up_position,null);


                //add if null or empty square
                if (board.getPiece(up_position) == null) {
                    j++;
                    list_moves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    j++;
                    continue;
                }
                //add possibe move and then break
                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    j++;
                    continue;
                }
                //update new or copy to the left
                new_position = myPosition;
            }
            j=1;

            //upright
            for (int i = 2; i > 0; i--){
                int new_row = new_position.getRow() + j;
                int col_right = new_position.getColumn() + i;
                //int col_right = new_position.getColumn() + 1;

                //base case if it goes too far right or too high up
                if(new_row > 8 || col_right > 8) {
                    j++;
                    new_position = myPosition;
                    continue;
                }

                //new chess piece position it can maybe go to
                ChessPosition up_position = new ChessPosition(new_row, col_right);

                //new moves
                ChessMove moves = new ChessMove(myPosition, up_position,null);


                //add if null or empty square
                if (board.getPiece(up_position) == null) {
                    j++;
                    list_moves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    j++;
                    continue;
                }
                //add possibe move and then break
                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    j++;
                    continue;
                }
                //update new or copy to the left
                new_position = myPosition;
            }

            j=1;

            //down_right
            for (int i = 2; i > 0; i--){
                int new_row = new_position.getRow() - j;
                int col_right = new_position.getColumn() + i;
                //int col_right = new_position.getColumn() + 1;

                //base case if it goes too far right or too high up
                if(new_row < 1 || col_right > 8) {
                    j++;
                    new_position = myPosition;
                    continue;
                }

                //new chess piece position it can maybe go to
                ChessPosition up_position = new ChessPosition(new_row, col_right);

                //new moves
                ChessMove moves = new ChessMove(myPosition, up_position,null);


                //add if null or empty square
                if (board.getPiece(up_position) == null) {
                    j++;
                    list_moves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    j++;
                    continue;
                }
                //add possibe move and then break
                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    j++;
                    continue;
                }
                //update new or copy to the left
                new_position = myPosition;
            }
            j=1;

            //down_left
            for (int i = 2; i > 0; i--){
                int new_row = new_position.getRow() - j;
                int col_left = new_position.getColumn() - i;
                //int col_right = new_position.getColumn() + 1;

                //base case if it goes too far left or too high up
                if(new_row < 1 || col_left < 1) {
                    j++;
                    new_position = myPosition;
                    continue;
                }

                //new chess piece position it can maybe go to
                ChessPosition up_position = new ChessPosition(new_row, col_left);

                //new moves
                ChessMove moves = new ChessMove(myPosition, up_position,null);


                //add if null or empty square
                if (board.getPiece(up_position) == null) {
                    j++;
                    list_moves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    new_position = myPosition;
                    j++;
                    continue;
                }
                //add possibe move and then break
                else if(board.getPiece(up_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    list_moves.add(moves);
                    new_position = myPosition;
                    j++;
                    continue;
                }
                //update new or copy to the left
                new_position = myPosition;
            }

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


            //Bishop moveset
            boolean left_up = true;
            boolean right_up = true;
            boolean down_left = true;
            boolean down_right = true;

            ChessPosition new_position = myPosition;

            //Rook Moveset
            boolean left = true;
            boolean right = true;
            boolean up = true;
            boolean down = true;
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

            while(down == true) {
                int new_row = new_position.getRow() - 1;
                ChessPosition up_position = new ChessPosition(new_row, myPosition.getColumn());

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if (new_row < 1) {
                    new_position = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, up_position, null);

                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(up_position) == null) {
                    list_moves.add(moves);
                } else if (board.getPiece(up_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    new_position = myPosition;
                    break;
                } else if (board.getPiece(up_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    list_moves.add(moves);
                    new_position = myPosition;
                    break;
                }
                //update new or copy to the left
                new_position = up_position;
            }

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
        return list_moves;
        //return new ArrayList<>();
    }
}
