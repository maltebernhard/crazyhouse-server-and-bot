package gameserver.model.crazyhouse;

import java.util.Arrays;

import org.junit.Before;

import gameserver.control.GameController;
import gameserver.model.Game;
import gameserver.model.Player;
import gameserver.model.User;

public class Test {
	
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

	public static void main(String[] args) {
		Test test = new Test();
		try {
			test.setUp();
		} catch(Exception e) {
			System.out.println("Error");
		}
		
		// ================================ Test 1 =========================================
		
		String board[] = {"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",
				"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/Pp",
				"RnBqKbNr/ppPppP1p/5Pp1/8/8/8/PpPPPpPP/R1BQkBNR/QPnr",
				"rnbqkbnr/pppprppp/8/8/8/8/PPPP1PPP/RNBRKRNR/P",
				"4k3/pppppppp/pppppppp/pppppppp/pppppppp/pppppppp/p1pppppp/K7/P"};
		String haskellerg[] = {"[a2-a3,a2-a4,b2-b3,b2-b4,c2-c3,c2-c4,d2-d3,d2-d4,e2-e3,e2-e4,f2-f3,f2-f4,g2-g3,g2-g4,h2-h3,h2-h4,b1-c3,b1-a3,g1-h3,g1-f3]",
				"[P-a6,P-b6,P-c6,P-d6,P-e6,P-f6,P-g6,P-h6,P-a5,P-b5,P-c5,P-d5,P-e5,P-f5,P-g5,P-h5,P-a4,P-b4,P-c4,P-d4,P-e4,P-f4,P-g4,P-h4,P-a3,P-b3,P-c3,P-d3,P-e3,P-f3,P-g3,P-h3,a2-a3,a2-a4,b2-b3,b2-b4,c2-c3,c2-c4,d2-d3,d2-d4,e2-e3,e2-e4,f2-f3,f2-f4,g2-g3,g2-g4,h2-h3,h2-h4,b1-c3,b1-a3,g1-h3,g1-f3]",
				"[e8-d8,c7-d8]",
				"[P-e6,P-e5,P-e4,P-e3,P-e2,g1-e2]",
				"[P-b2]"};
		
		boolean res;
		char javaCharray[];
		char haskellCharray[];

		for (int i=0; i<board.length; i++) {
			res = true;
			test.startGame(board[i],true);
			javaCharray = (test.valMoves(board[i])).toCharArray();
			Arrays.sort(javaCharray);
			haskellCharray = haskellerg[i].toCharArray();
			Arrays.sort(haskellCharray);
			for (int j = 0; j<javaCharray.length; j++) {
				if (javaCharray[j]!=haskellCharray[j]) res = false;
			}
			System.out.println("Test " + (i+1) +": "+(res && javaCharray.length==haskellCharray.length));
		}
	}
	
	private String valMoves(String boardstring) {
		String move = "";
		String string = "[";
		char convert1;
		char convert2;
		for (int i = 0 ; i<8 ; i++) {
			for (int j = 0 ; j<8 ; j++) {
				for (int k = 0 ; k<8 ; k++) {
					for (int l = 0 ; l<8 ; l++) {
						convert1 = (char)('a'+j);
						convert2 = (char)('a'+l);
						move = Character.toString(convert1) + Character.toString((char)('1'+i)) + "-" + Character.toString(convert2) + Character.toString((char)('1'+k));	
						startGame(boardstring,true);
						if (game.tryMove(move,whitePlayer)) string += move + ",";						
					}
				}
				for (char c : "prnbqkPRNBQK".toCharArray()) {
					convert1 = (char)('a'+j);
					move = Character.toString(c) + "-" + Character.toString(convert1) + Character.toString((char)('1'+i));
					startGame(boardstring,true);
					if (game.tryMove(move,whitePlayer)){				
						string += move + ",";
					}
				}
			}
		}
		return (string.substring(0,Math.max(1,string.length()-1))+"]");
	}
}
