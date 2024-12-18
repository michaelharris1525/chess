package websocket.commands;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    private final ChessMove move;

    private final String whiteblack;

    private ChessPosition position;

    public UserGameCommand(CommandType commandType,
                           String authToken, Integer gameID,
                           ChessMove move,String whiteblack, ChessPosition pos) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = move;
        this.whiteblack = whiteblack;
        this.position = pos;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN,
        VALID,
        DISPLAY
    }

    public ChessPosition getPosition() {
        return position;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getWhiteblack() {
        return whiteblack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand)) {
            return false;
        }
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    public ChessMove getRealMove() {
        return move;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}
