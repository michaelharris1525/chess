package server;

import chess.ChessBoard;
import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class Service {
    //REGISTER
    public AuthData register(UserData user, UserDataAcess dataobj, AuthTokenDataAcess authDataAcessobj) throws UserAlreadyExistsException {
        //on your handler do some checking in handler on server for registering
        AuthData authdata = new AuthData(UUID.randomUUID().toString(), user.username());

        //check if the username is in the database or not, should return null if its not in there
        if(dataobj.getuserdata(user.username()) != null){
            //if it doesn't work throw an exception
            throw new UserAlreadyExistsException("User already exists: ");
        }
        //add to database if there is no username, and auth data needs to
        dataobj.adduserdata(user);

        //create auth data token and add it to database
        authDataAcessobj.addAuthToken(authdata);

        return authdata;
    }

    //LOGIN
    public AuthData loginuser(UserData user,
                              UserDataAcess dataobj,
                              AuthTokenDataAcess authDataAcessobj) throws UserNameIsWrong,
            UserPasswordIsWrong, UserNameIsNullinMemoryDao {
        //on your handler do some checking in handler on server for registering
        AuthData authdata = new AuthData(UUID.randomUUID().toString(), user.username());

        if(!dataobj.getusersname(user.username()).equals(user.username())) {
            //if user doesn't match throw an exception
            throw new UserNameIsWrong("Error: unauthorized");
        }
        if(!BCrypt.checkpw(user.password(), dataobj.getuserpassword(user.username()))){
            throw new UserPasswordIsWrong("Error: unauthorized");
   }
        //create auth data token and add it to database
        authDataAcessobj.addAuthToken(authdata);
        return authdata;
    }

    //LOGOUT
    public void logout(String authtoken, AuthTokenDataAcess authdataac) throws DataAccessException {
        if(authdataac.getauthtoken(authtoken) == null){
            throw new DataAccessException("Messed up");
        }

        authdataac.deleteauthtoken(authtoken);
    }

    public int createGame(String auth, AuthTokenDataAcess authdataac,
                    GameDataAccess gameStoraged, String nameOfGame) throws DataAccessException {
        //validate AuthToken, check if auth data is null or not
        if(authdataac.getauthtoken(auth) == null){
            throw new DataAccessException("you screwed up");
        }
        //   private TeamColor currentColor;
        //    private TeamColor wColor;
        //    private TeamColor bColor;
        //    private ChessBoard board;
//        ChessBoard board = new ChessBoard();
//        board.resetBoard();
        ChessGame game = new ChessGame();

        //if validated create new game
        int newGameId = gameStoraged.getSize() + 1;
        gameStoraged.addNewGame(newGameId, nameOfGame, game);


        return newGameId;
    }
    public void joinGame(String authToken,
                         AuthTokenDataAcess authdataac,
                  GameDataAccess gameStoraged,
                         GameData gameDATAFROMREQUEST,
                         JoinGameRequest joinRequest)
            throws DataAccessException, PlayerColorException,
            BadRequestsException {
        if(authdataac.getauthtoken(authToken) == null){
            throw new DataAccessException("you screwed up");
        }

        if (gameDATAFROMREQUEST == null) {
            throw new BadRequestsException("GAMEDATAFROMREQUEST");
        }

        String playerColor = joinRequest.getPlayerColor();
        //grab authdata to get username
        AuthData authdataforusername = authdataac.getauthtoken(authToken);

        if (("WHITE".equals(playerColor) && gameDATAFROMREQUEST.getWhiteColor() != null) ||
                ("BLACK".equals(playerColor) && gameDATAFROMREQUEST.getBlackColor() != null)) {
            throw new PlayerColorException("Problem with white and player color, already taken");
        }
        if ("WHITE".equals(playerColor)) {
            joinRequest.updatePlayerWhite();
            gameStoraged.updateWhiteColor(gameDATAFROMREQUEST.gameID(),authdataforusername.username());// set WHITE player
        }
        else if ("BLACK".equals(playerColor)) {
            joinRequest.updatePlayerBlack();
            gameStoraged.updateBlackColor(gameDATAFROMREQUEST.gameID(),authdataforusername.username()); // set BLACK player
        }

    }

    // CLEAR, make 11 unit tests
    public void clearAllData(UserDataAcess userd, AuthTokenDataAcess authdata, GameDataAccess gameData){
        userd.clearuserdatabase();
        authdata.clearuserdatabase();
        gameData.clearGameData();
    }

}