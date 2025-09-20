package gameserver.control;

import gameserver.model.Game;
import gameserver.model.HaskellBot;
import gameserver.model.User;
import gameserver.model.crazyhouse.CrazyhouseGame;

public class GameFactory {

    //TODO: change path to bot executable if desired   
    public static final String CRAZYHOUSE_BOT_PATH = "C:\\Users\\malte\\Documents\\03 Bildung\\03.01 Uni\\Module\\7. Semester\\Softwaretechnik und Programmierparadigmen\\HA\\HA2\\Crazyhouse_Project_Java\\";
    public static final String CRAZYHOUSE_BOT_COMMAND = "Bot.exe";

    private GameFactory() {
    }

    public static Game createGame(String gameType) throws Exception {
        try {
            switch (gameType) {
            	// reduced to current game here
                case "crazyhouse":
                    return new CrazyhouseGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("Illegal game type encountered");
    }

    public static User createBot(String type, Game game) {
        if (type.equals("haskell")) {
            switch (game.getClass().getName().substring(game.getClass().getName().lastIndexOf(".") + 1)) {
                case "CrazyhouseGame":
                    return new HaskellBot(game, CRAZYHOUSE_BOT_PATH, CRAZYHOUSE_BOT_COMMAND);
                default:
                    return null;
            }
        }
        return null;
    }

}
