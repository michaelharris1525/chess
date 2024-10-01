package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor current_color;
    private TeamColor w_color;
    private TeamColor b_color;
    private ChessBoard board;
    public ChessGame() {
        this.current_color = TeamColor.WHITE;
        this.w_color = TeamColor.WHITE;
        this.b_color = TeamColor.BLACK;
        this.board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return current_color;

    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        //just flip the colors to the opposite when this functin is called
        if(TeamColor.WHITE == team) {
            team = b_color;
        }
        else{
            team = w_color;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }
    public Collection<ChessMove> find_valid_moves(TeamColor teamColor, ChessPiece n_piece,
                                                  Collection<ChessMove> list_moves_deletelater) {
        // King's position
        ChessPosition kings_position = find_king(teamColor, board);
        // Opposite color
        TeamColor Opposite_color = flip_colors(teamColor);
        // List of moves the piece can make
        List<ChessMove> moves_list = new ArrayList<>(list_moves_deletelater);
        // List of valid moves
        Collection<ChessMove> valid_moves = new ArrayList<>();
        ChessBoard copy_of_board = board.copy_board();
        // Iterate through every possible move your piece and color can make,
        // make that move and use a copy of board to see if it works
        for (int num = 0; num < moves_list.size(); num++) {
            ChessMove c_move = moves_list.get(num);  // Chess move for your team color
            copy_of_board = board.copy_board();  // Copy the board for this move simulation
            boolean isKingInCheck = false;  // Indicates whether the move puts the king in check

            // Make the move on the copied board
            makeMove_copy(c_move, copy_of_board);
            ChessBoard new_copy = copy_of_board.copy_board();

            // Check if any move by the opposing pieces puts the king in check
            outer_loop:
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = copy_of_board.getPiece(pos);

                    if (piece != null && piece.getTeamColor().equals(Opposite_color)) {
                        // Get all possible moves for the opponent's piece
                        Collection<ChessMove> opponentMoves = piece.pieceMoves(copy_of_board, pos);
                        for (ChessMove opponentMove : opponentMoves) {
                            // If this move puts the king in check, mark it as invalid
                            //makeMove_copy(opponentMove, copy_of_board);
                            if (isInCheck_king(teamColor, copy_of_board)) {
                                isKingInCheck = true;
                                break outer_loop;  // Stop checking further once the king is in check
                            }

                            // Restore board to its state before the move
                            copy_of_board = new_copy.copy_board();  // Re-copy the board to restore the state
                        }
                    }
                }
            }

            // If the move does not put the king in check, add it to the list of valid moves
            if (!isKingInCheck) {
                valid_moves.add(c_move);
            }
        }

        return valid_moves;
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //grabs piece at board location
        ChessPiece new_piece = board.getPiece(startPosition);
        //list of moves that one piece can move
        Collection<ChessMove>empty_list_moves = new ArrayList<>();
        Collection<ChessMove>king_list_moves = new ArrayList<>();

        Collection<ChessMove>list_moves = new_piece.pieceMoves(board,startPosition);
        //if piece is a king
        if (new_piece != null) {
        if(new_piece.getPieceType().equals(ChessPiece.PieceType.KING)) {
            //if piece has not moved and is not in check go in here
            if (isInCheck(new_piece.getTeamColor()) == false) {
                //if piece moves and is not in check do this
                ChessBoard copy_of_board = board.copy_board();
                list_moves = new_piece.pieceMoves(copy_of_board, startPosition);
                List<ChessMove>list_moves_before_change = new ArrayList<>(list_moves);
                for(int s = 0; s< list_moves_before_change.size(); s++){
                    ChessMove king_move = list_moves_before_change.get(s);
                    makeMove_copy(list_moves_before_change.get(s), copy_of_board);
                    if(!isInCheck_king(new_piece.getTeamColor(), copy_of_board)) {
                        king_list_moves.add(list_moves_before_change.get(s));
                        copy_of_board = board.copy_board();
                    }
                    copy_of_board = board.copy_board();
                }
                return king_list_moves;
            }
            else {
                list_moves = find_valid_moves(new_piece.getTeamColor(), new_piece, list_moves);
                return list_moves;
            }
        }
        //if a piece is not a king
        else{
            //if piece has not moved and is not in check go in here
            if (isInCheck(new_piece.getTeamColor()) == false) {
                //if piece moves and is not in check do this
                ChessBoard copy_of_board = board.copy_board();

                List<ChessMove>new_list_moves = new ArrayList<>();
                list_moves = new_piece.pieceMoves(copy_of_board, startPosition);
                List<ChessMove>list_moves_before_change = new ArrayList<>(list_moves);

                for(int s = 0; s< list_moves_before_change.size(); s++){
                    makeMove_copy(list_moves_before_change.get(s), copy_of_board);
                    if(!isInCheck_king(new_piece.getTeamColor(), copy_of_board)) {
                        //since in check, is there a move that takes a piece?
                        // if so is the king still in check after piece is taken
                        new_list_moves.add(list_moves_before_change.get(s));
                    }
                    copy_of_board = board.copy_board();
                }
                return new_list_moves;
            }
            else {
                //crashes here
                list_moves = find_valid_moves(new_piece.getTeamColor(), new_piece, list_moves);
                return list_moves;
             }
        }
        }
        return list_moves;
    }
    boolean is_inside_validmoves(ChessMove move_check) {
        //list of moves the piece can make
        Collection<ChessMove>valid_moves = validMoves(move_check.getStartPosition());
        List<ChessMove> valid_moves_list = new ArrayList<>(valid_moves);
        //list of valid moves
        for (int num = 0; num < valid_moves_list.size(); num++) {
            ChessMove move_to_compare = valid_moves_list.get(num);
            if(valid_moves_list.equals(move_check)){
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //throw new RuntimeException("Not implemented");
        //if(validMoves() is in move)
        try {
            ChessPosition start_pos = move.getStartPosition();
            ChessPosition end_pos = move.getEndPosition();
            //make a new piece
            ChessPiece new_piece = board.getPiece(start_pos);
            //delete piece whether null or not, remove it. Then add piece to board
            board.removePiece(end_pos);
            board.addPiece(end_pos, new_piece);
            //delete the piece where it was at before
            board.removePiece(start_pos);
        }
        catch(Exception e) {
            System.out.println("Invalid move: " + e.getMessage());
        }

    }
    public void makeMove_copy(ChessMove move, ChessBoard copy_board)  {
        //throw new RuntimeException("Not implemented");
        ChessPosition start_pos = move.getStartPosition();
        ChessPosition end_pos = move.getEndPosition();
        //make a new piece from original board
        ChessPiece new_piece = copy_board.getPiece(start_pos);
        //delete piece whether null or not, remove it. Then add piece to board
        copy_board.addPiece(end_pos, new_piece);
        //copy_board.removePiece(end_pos);
        //delete the piece where it was at before
        copy_board.removePiece(start_pos);

    }

    public TeamColor flip_colors(TeamColor teamColor) {
        if (teamColor == w_color) {
            return b_color;
        }
        return w_color;
    }

    public ChessPosition find_king(TeamColor teamColor, ChessBoard copy_or_og){
        ChessPiece what_imlookingfor = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition king_position = new ChessPosition(1,1);
        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++){
                //goes through the entire board to check to see if that piece is a king, and if it is return the position of the king
                ChessPosition new_pos = new ChessPosition(row, col);
                ChessPiece tf_king = copy_or_og.getPiece(new_pos);
                if(what_imlookingfor.equals(tf_king)){
                    //king_position = new_pos;
                    return new_pos;
                }
            }
        }
        return king_position;
    }



    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */


        public boolean isInCheck(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        //run all possible pieces on opposite color of teamColor and see if any piece specifically hits the king, and if it does, return true
        ChessPosition kings_position = find_king(teamColor, board);
        //opposite color
        TeamColor Opposite_color = flip_colors(teamColor);
        //all moves that lead to check
        //Collection<ChessMove> all_moves_in_check = new ArrayList<>();
        //
        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++) {
                ChessPosition pos_lookinat = new ChessPosition(row, col);
                //section on github of copying objects
                ChessBoard copy_of_board = board;
                ChessPiece piece_lookinat = copy_of_board.getPiece(pos_lookinat);
                //if piece matches the oppposing player's piece then we care about if its in check or not
                //fix this line, for some reason no likey this if statement

                if (piece_lookinat != null) {
                    if (piece_lookinat.getTeamColor().equals(Opposite_color)) {
                        piece_lookinat.pieceMoves(copy_of_board, pos_lookinat);
                        Collection<ChessMove> list_moves = piece_lookinat.pieceMoves(copy_of_board, pos_lookinat);
                        List<ChessMove> moves_list = new ArrayList<>(list_moves);
                        for (int num = 0; num < moves_list.size(); num++) {
                            ChessMove c_move = moves_list.get(num);
                            if (c_move.getEndPosition().equals(kings_position)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove>empty_moves = new ArrayList<>();
        Collection<ChessMove>edited_moves = new ArrayList<>();
        ChessBoard copy_board = board.copy_board();

        if(isInCheck_king(teamColor, board)) {
            Collection<ChessMove> kings_moves = king.pieceMoves(board, find_king(teamColor, board));
            List<ChessMove>kings_list = new ArrayList<>(kings_moves);
            //all possible moves king can move
            for(int i = 0; i<1;i++) {
                ChessMove c_move = kings_list.get(i);
                edited_moves = validMoves(c_move.getStartPosition());
            }
            //if list is empty, go through all of same colors moves and see if king is in check
            //if king is not in check after move in copy board return false
            if(edited_moves.equals(empty_moves)){
                for(int row = 1; row <=8; row++) {
                    for (int col = 1; col <= 8; col++) {
                        ChessPosition new_pos = new ChessPosition(row, col);
                        ChessPiece your_piece = board.getPiece(new_pos);

                        if (your_piece != null) {
                            if (your_piece.getTeamColor().equals(teamColor)) {
                                Collection<ChessMove> your_collection = your_piece.pieceMoves(board, new_pos);
                                List<ChessMove> list_your_pmoves = new ArrayList<>(your_collection);
                                for (ChessMove yourMove : list_your_pmoves) {
                                    makeMove_copy(yourMove, copy_board);
                                    if (!isInCheck_king(teamColor, copy_board)) {
                                        return false;
                                    }
                                    copy_board = board.copy_board();
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }

        return false;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        board.resetBoard();
        return this.board;
    }

    public boolean isInCheck_king(TeamColor teamColor, ChessBoard copy_of_board) {
        //throw new RuntimeException("Not implemented");
        //run all possible pieces on opposite color of teamColor and see if any piece specifically hits the king, and if it does, return true
        ChessPosition kings_position = find_king(teamColor,copy_of_board);
        //opposite color
        TeamColor Opposite_color = flip_colors(teamColor);
        //all moves that lead to check
        //Collection<ChessMove> all_moves_in_check = new ArrayList<>();
        //
        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++) {
                ChessPosition pos_lookinat = new ChessPosition(row, col);
                //section on github of copying objects
                //ChessBoard copy_of_board = board;
                ChessPiece piece_lookinat = copy_of_board.getPiece(pos_lookinat);
                //if piece matches the oppposing player's piece then we care about if its in check or not
                //fix this line, for some reason no likey this if statement

                if (piece_lookinat != null) {
                    if (piece_lookinat.getTeamColor().equals(Opposite_color)) {
                        piece_lookinat.pieceMoves(copy_of_board, pos_lookinat);
                        Collection<ChessMove> list_moves = piece_lookinat.pieceMoves(copy_of_board, pos_lookinat);
                        List<ChessMove> moves_list = new ArrayList<>(list_moves);
                        for (int num = 0; num < moves_list.size(); num++) {
                            ChessMove c_move = moves_list.get(num);
                            if (c_move.getEndPosition().equals(kings_position)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
