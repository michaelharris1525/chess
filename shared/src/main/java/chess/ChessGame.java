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

    public Collection<ChessMove> find_valid_moves(TeamColor teamColor, ChessPiece n_piece,Collection<ChessMove>list_moves_deletelater) {
        //kings position
        ChessPosition kings_position = find_king(teamColor);
        //opposite color
        TeamColor Opposite_color = flip_colors(teamColor);
        //list of moves the piece can make
        List<ChessMove> moves_list = new ArrayList<>(list_moves_deletelater);
        //list of valid moves
        Collection<ChessMove> valid_moves = new ArrayList<>();

        //iterate through every possible move your piece and color can make,
        //make that move and use copy of board to see if it works
        for (int num = 0; num < moves_list.size(); num++) {
            //chess move for your team color (ex: this is white)
            ChessMove c_move = moves_list.get(num);
            //section on github of copying objects
            ChessBoard copy_of_board = board;
            //if the piece moves it breaks
            boolean tf_break = false;

            //make move on copy of board and determine if the king is in check or not
            makeMove_copy(c_move,copy_of_board);

            //all possible values for opposing pieces (ex: this is black)
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    //position and piece we'll be using
                    ChessPosition pos_lookinat = new ChessPosition(row, col);
                    ChessPiece piece_lookinat = copy_of_board.getPiece(pos_lookinat);
                    //if piece matches the oppposing player's piece then we care about
                    // if one of its moves is in check or not, if it is then you break
                    // and don't add valid move
                    if (piece_lookinat.equals(Opposite_color)) {
                        piece_lookinat.pieceMoves(copy_of_board, pos_lookinat);
                        Collection<ChessMove> list_moves = piece_lookinat.pieceMoves(copy_of_board, pos_lookinat);
                        List<ChessMove> moves_list_newpiece = new ArrayList<>(list_moves);
                        for (int i = 0; num < moves_list_newpiece.size(); i++) {
                            ChessMove chessssmove = moves_list.get(i);
                            if (chessssmove.getEndPosition().equals(kings_position)) {
                                tf_break = true;
                            }
                        }
                    }
                }
            }
            if(tf_break == false) {
                valid_moves.add(c_move);
            }
        }
        return valid_moves;

//        for (int num = 0; num < moves_list.size(); num++) {
//            ChessMove c_move = moves_list.get(num);
//        }
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
        Collection<ChessMove>list_moves = new_piece.pieceMoves(board,startPosition);

        if (isInCheck(new_piece.getTeamColor()) == false) {
            list_moves = new_piece.pieceMoves(board,startPosition);
            return list_moves;
        }
        else{
            list_moves= find_valid_moves(new_piece.getTeamColor(),new_piece,list_moves);
            return list_moves;
        }
    }
    boolean is_inside_validmoves(ChessMove move_check) {
        //list of moves the piece can make
        Collection<ChessMove>valid_moves = validMoves(move_check.getStartPosition());
        List<ChessMove> valid_moves_list = new ArrayList<>(valid_moves);
        //list of valid moves
        for (int num = 0; num < valid_moves_list.size(); num++) {
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
        //make a new piece
        ChessPiece new_piece = board.getPiece(start_pos);
        //delete piece whether null or not, remove it. Then add piece to board
        copy_board.removePiece(end_pos);
        copy_board.addPiece(end_pos, new_piece);
        //delete the piece where it was at before
        copy_board.removePiece(start_pos);

    }

    public TeamColor flip_colors(TeamColor teamColor) {
        if (teamColor == w_color) {
            return b_color;
        }
        return w_color;
    }

    public ChessPosition find_king(TeamColor teamColor){
        ChessPiece what_imlookingfor = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition king_position = new ChessPosition(1,1);
        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++){
                //goes through the entire board to check to see if that piece is a king, and if it is return the position of the king
                ChessPosition new_pos = new ChessPosition(row, col);
                ChessPiece tf_king = board.getPiece(new_pos);
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
        ChessPosition kings_position = find_king(teamColor);
        //opposite color
        TeamColor Opposite_color = flip_colors(teamColor);
        //all moves that lead to check
        //Collection<ChessMove> all_moves_in_check = new ArrayList<>();

        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++) {
                ChessPosition pos_lookinat = new ChessPosition(row, col);
                ChessPiece piece_lookinat = board.getPiece(pos_lookinat);
                //if piece matches the oppposing player's piece then we care about if its in check or not
                if(piece_lookinat.equals(Opposite_color)){
                    piece_lookinat.pieceMoves(board,pos_lookinat);
                    Collection<ChessMove>list_moves = piece_lookinat.pieceMoves(board,pos_lookinat);
                    List<ChessMove> moves_list = new ArrayList<>(list_moves);
                    for(int num=0; num < moves_list.size(); num++) {
                        ChessMove c_move = moves_list.get(num);
                        if(c_move.getEndPosition().equals(kings_position)){
                            return true;
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
        if(king.pieceMoves(board, find_king(teamColor)) == empty_moves) {
            return true;
        }
        else{
            return false;
        }
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
        return this.board;
    }
}
