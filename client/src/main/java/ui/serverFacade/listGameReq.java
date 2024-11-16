package ui.serverFacade;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class listGameReq {
    private String gameName;
    private Map<String, Collection<GameData>> responseObj = new HashMap<>();

    public listGameReq(String name) {
        this.gameName = name;
    }

}
