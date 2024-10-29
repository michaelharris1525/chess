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
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;

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

    public boolean pawnWhitePromotionCheck(int row) {
        if (row == 8 || row == 1){
            return true;
        }
        return false;
    }
    public void createAllPromotionalPieces(ChessPosition myPosition, ChessPosition endingPositionition,
                                             Collection<ChessMove>listMoves) {
        ChessPiece.PieceType[]allTypes = ChessPiece.PieceType.values();
        for(ChessPiece.PieceType type: allTypes) {
            if(type == PieceType.KING || type == PieceType.PAWN) {
                continue;
            }
            ChessMove promotionMove = new ChessMove(myPosition, endingPositionition, type);
            listMoves.add(promotionMove);
        }
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        /* Thoughts before coding: only showing valid moves,
        don't worry about if other pieces are in the way for now
        also make a good helper function to make sure your pieces are in bounds*/
        Collection<ChessMove>listMoves = new ArrayList<>();

        if(this.pieceType == PieceType.PAWN){
            /* if and only if move diagonal if opposite color is on those sides
            * movement is row+1 always and depending on if there are pieces diagonally col +1 or -1
            *  */
            int newRow = myPosition.getRow();
            int newCol = myPosition.getColumn();
            boolean boolPawnWhite = false;
            if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
                boolPawnWhite = true;
            }


            if(boolPawnWhite == true) {
                //check to see if pawn has moved or not by comparing all the white pawns on the reset board in the CHESSBOARD CLASS

                if(myPosition.getRow() == 2) {
                    ChessPosition d1 = new ChessPosition(newRow+1, newCol);
                    ChessPosition d2 = new ChessPosition(newRow+2,newCol);

                    //check to see if you can move two spaces up
                    if(board.getPiece(d1) == null && board.getPiece(d2) == null) {
                        ChessPosition pNotMovedMove = new ChessPosition(newRow + 2, newCol);
                        ChessMove notMove = new ChessMove(myPosition, pNotMovedMove, null);
                        listMoves.add(notMove);
                    }
                }
                //pawn rules of movement
                for (int i = 0; i < 3; i++) {
                    if (newRow > 8 || newCol < 1 || newCol > 8) {
                        continue;
                    }
                    //new chess piece position it can maybe go to
                    if (i == 0 || i == 2) {
                        int baseCol = newCol + (i-1);
                        ChessPosition leftUpPosition = new ChessPosition(newRow + 1,
                                baseCol);
                        if (newRow > 8 || baseCol < 1 || baseCol > 8) {
                            continue;
                        }
                        ChessMove basecaseMove = new ChessMove(myPosition, leftUpPosition, null);
                        if (board.getPiece(leftUpPosition) == null ||
                                (board.getPiece(leftUpPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor())) {
                            continue;
                        } else if (board.getPiece(leftUpPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            if(pawnWhitePromotionCheck(leftUpPosition.getRow()) == true){
                                createAllPromotionalPieces(myPosition,leftUpPosition,listMoves);
                                continue;
                            }
                            listMoves.add(basecaseMove);
                            continue;
                        }
                    }
                    ChessPosition newPosition = new ChessPosition(newRow + 1, newCol + (i - 1));
                    //new move
                    ChessMove moves = new ChessMove(myPosition, newPosition, null);
                    if (board.getPiece(newPosition) == null) {
                        if(pawnWhitePromotionCheck(newPosition.getRow()) == true){
                            createAllPromotionalPieces(myPosition,newPosition,listMoves);
                            newPosition = myPosition;
                            continue;
                        }
                        listMoves.add(moves);
                        newPosition = myPosition;
                        continue;
                    }
                    // If the square has a piece of the same color, stop further checks in this direction
                    else if (board.getPiece(newPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        newPosition = myPosition;
                        continue;
                    } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        if (i==1) {
                            continue;
                        }
                        listMoves.add(moves);
                        newPosition = myPosition;
                        continue;
                    }

                }
            }
            //else if pawn is black
            else {
                //check to see if pawn has moved or not
                if(myPosition.getRow() == 7) {
                    //base case if there is a piece in the way (white or black) 1 space up or down then continue. Not possible.
                    ChessPosition d1 = new ChessPosition(newRow-1, newCol);
                    ChessPosition d2 = new ChessPosition(newRow-2,newCol);

                    if(board.getPiece(d1) == null && board.getPiece(d2) == null) {
                        ChessPosition pNotMovedMove = new ChessPosition(newRow - 2, newCol);
                        ChessMove notMove = new ChessMove(myPosition, pNotMovedMove, null);
                        listMoves.add(notMove);
                    }
                }

                //pawn rules of movement
                for (int i = 0; i < 3; i++) {
                    if (newRow < 1 || newCol < 1 || newCol > 8) {
                        continue;
                    }
                    //new chess piece position it can maybe go to
                    if (i == 0 || i ==2) {
                        int baseCol = newCol + (i-1);
                        ChessPosition leftDownPosition = new ChessPosition(newRow-1,
                                baseCol);
                        if (newRow < 1 || baseCol < 1 || baseCol > 8) {
                            continue;
                        }
                        ChessMove basecaseMove = new ChessMove(myPosition, leftDownPosition, null);
                        if (board.getPiece(leftDownPosition) == null ||
                                (board.getPiece(leftDownPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor())) {
                            continue;
                        }
                        else if (board.getPiece(leftDownPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            if(pawnWhitePromotionCheck(leftDownPosition.getRow()) == true){
                                createAllPromotionalPieces(myPosition,leftDownPosition,listMoves);
                                continue;
                            }
                            listMoves.add(basecaseMove);
                            continue;
                        }
                    }
                    ChessPosition newPosition = new ChessPosition(newRow - 1, newCol + (i - 1));
                    //new move
                    ChessMove moves = new ChessMove(myPosition, newPosition, null);
                    if (board.getPiece(newPosition) == null) {
                        if(pawnWhitePromotionCheck(newPosition.getRow()) == true){
                            createAllPromotionalPieces(myPosition,newPosition,listMoves);
                            newPosition = myPosition;
                            continue;
                        }
                        listMoves.add(moves);
                        newPosition = myPosition;
                        continue;
                    }
                    // If the square has a piece of the same color, stop further checks in this direction
                    else if (board.getPiece(newPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        newPosition = myPosition;
                        continue;
                    }
                    else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        //if pawn is trying to go vertical color doesn't matter, skip
                        if (i==1) {
                            continue;
                        }
                        listMoves.add(moves);
                        newPosition = myPosition;
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
                int newRow = myPosition.getRow() + direction[0];
                int newCol = myPosition.getColumn() + direction[1];

                // Base case: Check if the position is out of bounds
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                    continue;
                }

                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessMove move = new ChessMove(myPosition, newPosition, null);

                // If the square is empty, add the move and stop further checks in this direction
                if (board.getPiece(newPosition) == null) {
                    listMoves.add(move);
                }
                // If the square has a piece of the same color, stop further checks in this direction
                else if (board.getPiece(newPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    continue;
                }
                // If the square has an opponent's piece, add the move and stop further checks in this direction
                else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    listMoves.add(move);
                }
            }


        }


        //how knight moves
        else if(this.pieceType == PieceType.KNIGHT) {
            boolean leftUp = true;
            boolean rightUp = true;
            boolean downLeft = true;
            boolean downRight = true;

            ChessPosition newPosition = myPosition;
            ChessMove move;
            int j = 1;

            //upleft
            for (int i = 2; i > 0; i--){

                int newRow = newPosition.getRow() + j;
                int colLeft = newPosition.getColumn() - i;
                //int colRight = newPosition.getColumn() + 1;

                //base case if it goes too far left or too high up
                if(newRow > 8 || colLeft < 1) {
                    j++;
                    newPosition = myPosition;
                    continue;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colLeft);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);


                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    j++;
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    j++;
                    continue;
                }
                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    j++;
                    continue;
                }
                //update new or copy to the left
                newPosition = myPosition;
            }
            j=1;

            //upright
            for (int i = 2; i > 0; i--){
                int newRow = newPosition.getRow() + j;
                int colRight = newPosition.getColumn() + i;
                //int colRight = newPosition.getColumn() + 1;

                //base case if it goes too far right or too high up
                if(newRow > 8 || colRight > 8) {
                    j++;
                    newPosition = myPosition;
                    continue;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colRight);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);


                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    j++;
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    j++;
                    continue;
                }
                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    j++;
                    continue;
                }
                //update new or copy to the left
                newPosition = myPosition;
            }

            j=1;

            //downRight
            for (int i = 2; i > 0; i--){
                int newRow = newPosition.getRow() - j;
                int colRight = newPosition.getColumn() + i;
                //int colRight = newPosition.getColumn() + 1;

                //base case if it goes too far right or too high up
                if(newRow < 1 || colRight > 8) {
                    j++;
                    newPosition = myPosition;
                    continue;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colRight);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);


                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    j++;
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    j++;
                    continue;
                }
                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    j++;
                    continue;
                }
                //update new or copy to the left
                newPosition = myPosition;
            }
            j=1;

            //downLeft
            for (int i = 2; i > 0; i--){
                int newRow = newPosition.getRow() - j;
                int colLeft = newPosition.getColumn() - i;
                //int colRight = newPosition.getColumn() + 1;

                //base case if it goes too far left or too high up
                if(newRow < 1 || colLeft < 1) {
                    j++;
                    newPosition = myPosition;
                    continue;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colLeft);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);


                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    j++;
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    j++;
                    continue;
                }
                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    j++;
                    continue;
                }
                //update new or copy to the left
                newPosition = myPosition;
            }

        }






        //how bishop moves
        else if(this.pieceType == PieceType.BISHOP) {
            //moves diagonal so +1 to the row and +1 or -1 for column if its moving up, if moving down its -1 row and -1 and +1 col
            boolean leftUp = true;
            boolean rightUp = true;
            boolean downLeft = true;
            boolean downRight = true;

            ChessPosition newPosition = myPosition;
            ChessMove move;

            //diagonal down left and right
            while(downLeft == true){
                int newRow = newPosition.getRow() - 1;
                int colLeft = newPosition.getColumn() - 1;

                //base case if it goes too far left or too far down
                if(newRow < 1 || colLeft < 1) {
                    newPosition = myPosition;
                    break;
                }


                //new chess piece position
                ChessPosition upPosition = new ChessPosition(newRow, colLeft);

                //new chess move
                ChessMove moves = new ChessMove(myPosition, upPosition,null);

                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }

                //if null do two team color stuff
                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }
                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }


                //update new or copy to the left
                newPosition = upPosition;
            }

            while(downRight == true){
                int newRow = newPosition.getRow() - 1;
                int colRight = newPosition.getColumn() + 1;

                //base case if it goes too far right or too far down
                if(newRow < 1 || colRight > 8) {
                    //reset position
                    newPosition = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colRight);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);

                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }


                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    listMoves.add(moves);
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }


            //diagonal up left and right
            while(leftUp == true){
                int newRow = newPosition.getRow() + 1;
                int colLeft = newPosition.getColumn() - 1;
                //int colRight = newPosition.getColumn() + 1;

                //base case if it goes too far left or too high up
                if(newRow > 8 || colLeft < 1) {
                    newPosition = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colLeft);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);


                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }
                //add if null or empty square
                else if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }
                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }
            while(rightUp == true){
                int newRow = newPosition.getRow() + 1;
//                int colLeft = newPosition.getColumn() - 1;
                int colRight = newPosition.getColumn() + 1;

                //base case if it goes to far right or up
                if(newRow > 8 || colRight > 8) {
                    newPosition = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colRight);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);

                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }

                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }

        }

        //how rook moves
        else if(this.pieceType == PieceType.ROOK){
            boolean left = true;
            boolean right = true;
            boolean up = true;
            boolean down = true;
            ChessPosition newPosition = myPosition;
            //find out where it is on the board, go to the left, up, right, down and return a list of array.

            //GOING LEFT
            while(left == true) {
                int newCol = newPosition.getColumn() - 1;

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(newCol < 1) {
                    newPosition = myPosition;
                    break;
                }

                //new chess position
                ChessPosition left_position = new ChessPosition(myPosition.getRow(), newCol);
                //new chess move
                ChessMove moves = new ChessMove(myPosition, left_position,null);

                if (board.getPiece(left_position) == null) {
                    listMoves.add(moves);
                }
                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                else if (board.getPiece(left_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }
                else if(board.getPiece(left_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = left_position;
            }


            //GOING RIGHT
            while(right == true) {
                int newCol = newPosition.getColumn() + 1;
                ChessPosition left_position = new ChessPosition(myPosition.getRow(), newCol);

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(newCol > 8) {
                    newPosition = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, left_position,null);

                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(left_position) == null) {
                    listMoves.add(moves);
                }
                else if (board.getPiece(left_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }
                else if(board.getPiece(left_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = left_position;
            }

            // GOING UP

            while(up == true){
                int newRow = newPosition.getRow() + 1;
                ChessPosition upPosition = new ChessPosition(newRow, myPosition.getColumn());

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(newRow > 8) {
                    newPosition = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, upPosition,null);



                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }

                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }

            //GOING DOWN

            while(down == true){
                int newRow = newPosition.getRow() - 1;
                ChessPosition upPosition = new ChessPosition(newRow, myPosition.getColumn());

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(newRow < 1) {
                    newPosition = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, upPosition,null);

                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }

                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }

            //(myPosition.getRow())(myPosition.getColumn())
        }

        //how queen moves
        else if(this.pieceType == PieceType.QUEEN){
            //Bishop moveset
            boolean leftUp = true;
            boolean rightUp = true;
            boolean downLeft = true;
            boolean downRight = true;

            ChessPosition newPosition = myPosition;

            //Rook Moveset
            boolean left = true;
            boolean right = true;
            boolean up = true;
            boolean down = true;
            //find out where it is on the board, go to the left, up, right, down and return a list of array.

            //GOING LEFT
            while(left == true) {
                int newCol = newPosition.getColumn() - 1;

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(newCol < 1) {
                    newPosition = myPosition;
                    break;
                }

                //new chess position
                ChessPosition left_position = new ChessPosition(myPosition.getRow(), newCol);
                //new chess move
                ChessMove moves = new ChessMove(myPosition, left_position,null);

                if (board.getPiece(left_position) == null) {
                    listMoves.add(moves);
                }
                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                else if (board.getPiece(left_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }
                else if(board.getPiece(left_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = left_position;
            }


            //GOING RIGHT
            while(right == true) {
                int newCol = newPosition.getColumn() + 1;
                ChessPosition left_position = new ChessPosition(myPosition.getRow(), newCol);

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(newCol > 8) {
                    newPosition = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, left_position,null);

                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(left_position) == null) {
                    listMoves.add(moves);
                }
                else if (board.getPiece(left_position).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }
                else if(board.getPiece(left_position).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = left_position;
            }

            // GOING UP

            while(up == true){
                int newRow = newPosition.getRow() + 1;
                ChessPosition upPosition = new ChessPosition(newRow, myPosition.getColumn());

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if(newRow > 8) {
                    newPosition = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, upPosition,null);



                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }

                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }

            //GOING DOWN

            while(down == true) {
                int newRow = newPosition.getRow() - 1;
                ChessPosition upPosition = new ChessPosition(newRow, myPosition.getColumn());

                //base case if it goes out of bounds, only have to worry about 1 since its only going left
                if (newRow < 1) {
                    newPosition = myPosition;
                    break;
                }

                //new chess move
                ChessMove moves = new ChessMove(myPosition, upPosition, null);

                //break if the board is out of bounds when a piece that is the same color of a piece is on that square on the chess board
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                } else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    newPosition = myPosition;
                    break;
                } else if (board.getPiece(upPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }

            //diagonal down left and right
            while(downLeft == true){
                int newRow = newPosition.getRow() - 1;
                int colLeft = newPosition.getColumn() - 1;

                //base case if it goes too far left or too far down
                if(newRow < 1 || colLeft < 1) {
                    newPosition = myPosition;
                    break;
                }


                //new chess piece position
                ChessPosition upPosition = new ChessPosition(newRow, colLeft);

                //new chess move
                ChessMove moves = new ChessMove(myPosition, upPosition,null);

                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }

                //if null do two team color stuff
                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }
                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }


                //update new or copy to the left
                newPosition = upPosition;
            }

            while(downRight == true){
                int newRow = newPosition.getRow() - 1;
                int colRight = newPosition.getColumn() + 1;

                //base case if it goes too far right or too far down
                if(newRow < 1 || colRight > 8) {
                    //reset position
                    newPosition = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colRight);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);

                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }


                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    listMoves.add(moves);
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }


            //diagonal up left and right
            while(leftUp == true){
                int newRow = newPosition.getRow() + 1;
                int colLeft = newPosition.getColumn() - 1;
                //int colRight = newPosition.getColumn() + 1;

                //base case if it goes too far left or too high up
                if(newRow > 8 || colLeft < 1) {
                    newPosition = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colLeft);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);


                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }
                //add if null or empty square
                else if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }
                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }
            while(rightUp == true){
                int newRow = newPosition.getRow() + 1;
//                int colLeft = newPosition.getColumn() - 1;
                int colRight = newPosition.getColumn() + 1;

                //base case if it goes to far right or up
                if(newRow > 8 || colRight > 8) {
                    newPosition = myPosition;
                    break;
                }

                //new chess piece position it can maybe go to
                ChessPosition upPosition = new ChessPosition(newRow, colRight);

                //new moves
                ChessMove moves = new ChessMove(myPosition, upPosition,null);

                //add if null or empty square
                if (board.getPiece(upPosition) == null) {
                    listMoves.add(moves);
                }

                //break if a piece is the same color of a piece is on that square on the chess board
                else if (board.getPiece(upPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                    newPosition = myPosition;
                    break;
                }

                //add possibe move and then break
                else if(board.getPiece(upPosition).getTeamColor()!=board.getPiece(myPosition).getTeamColor()){
                    listMoves.add(moves);
                    newPosition = myPosition;
                    break;
                }
                //update new or copy to the left
                newPosition = upPosition;
            }
        }
        return listMoves;
        //return new ArrayList<>();
    }
}
