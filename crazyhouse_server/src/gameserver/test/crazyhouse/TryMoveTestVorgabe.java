package gameserver.test.crazyhouse;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gameserver.control.GameController;
import gameserver.model.Game;
import gameserver.model.Player;
import gameserver.model.User;

public class TryMoveTestVorgabe {

	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	
	Player whitePlayer = null;
	Player blackPlayer = null;
	Game game = null;
	GameController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();
		
		int gameID = controller.startGame(user1, "", "crazyhouse");
		
		game =  controller.getGame(gameID);
		whitePlayer = game.getPlayer(user1);

	}
	
	public void startGame() {
		controller.joinGame(user2, "crazyhouse");		
		blackPlayer = game.getPlayer(user2);
	}
	
	public void startGame(String initialBoard, boolean whiteNext) {
		startGame();
		
		game.setBoard(initialBoard);
		game.setNextPlayer(whiteNext? whitePlayer:blackPlayer);
	}
	
	public void assertMove(String move, boolean white, boolean expectedResult) {
		if (white)
			assertEquals(expectedResult, game.tryMove(move, whitePlayer));
		else 
			assertEquals(expectedResult,game.tryMove(move, blackPlayer));
	}
	
	public void assertGameState(String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon) {
		String board = game.getBoard().replaceAll("e", "");
		
		assertEquals(expectedBoard,board);
		assertEquals(finished, game.isFinished());

		if (!game.isFinished()) {
			assertEquals(whiteNext, game.getNextPlayer() == whitePlayer);
		} else {
			assertEquals(whiteWon, whitePlayer.isWinner());
			assertEquals(!whiteWon, blackPlayer.isWinner());
		}
	}
	

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 *******************************************/
	
	@Test
	public void exampleTest() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b3",true,true);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/1P6/P1PPPPPP/RNBQKBNR/",false,false,false);
	}

	//TODO: implement test cases of same kind as example here
}
