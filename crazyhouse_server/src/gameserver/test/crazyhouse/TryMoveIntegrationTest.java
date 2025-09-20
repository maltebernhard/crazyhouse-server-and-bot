package gameserver.test.crazyhouse;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gameserver.control.GameController;
import gameserver.model.Game;
import gameserver.model.Player;
import gameserver.model.User;

public class TryMoveIntegrationTest {

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
		assertMove("b2-b7",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	//TODO: implement test cases of same kind as example here
	
	
	// ====================================== Wei�e Bauern Tests ===============================================
	
	// Zwei Schritte vorw�rts
	@Test
	public void Test1_1() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b4",true,true);
		assertGameState("rnbqkbnr/pppppppp/8/8/1P6/8/P1PPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Ein Schritt vorw�rts
	@Test
	public void Test1_2() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b3",true,true);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/1P6/P1PPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Zwei Schritte vorw�rts - gegnerischer Bauer im Weg
	@Test
	public void Test1_3() {
		startGame("rnbqkbnr/pppppppp/8/8/8/1p6/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b4",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/1p6/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Zwei Schritte vorw�rts und einen zur Seite (inkorrekter Zug)
	@Test
	public void Test1_4() {
		startGame("rnbqkbnr/pppppppp/8/8/8/1p6/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-c4",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/1p6/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Zwei Schritte vorw�rts - gegnerischer Bauer auf Zielfeld
	@Test
	public void Test1_5() {
		startGame("rnbqkbnr/pppppppp/8/8/1p6/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b4",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/1p6/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Drei Schritte vorw�rts
	@Test
	public void Test1_6() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b5",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Ein Schritt r�ckw�rts
	@Test
	public void Test1_7() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R1BQKBNR/",true);
		assertMove("b2-b1",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R1BQKBNR/",true,false,false);
	}
	
	// Gegnerischen Bauer schlagen
	@Test
	public void Test1_8() {
		startGame("rnbqkbnr/pppppppp/8/8/8/1p6/PPPPPPPP/RNBQKBNR/",true);
		assertMove("a2-b3",true,true);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/1P6/1PPPPPPP/RNBQKBNR/P",false,false,false);
	}
	
	// Ein Schritt nach vorne - Gegner im Weg
	@Test
	public void Test1_9() {
		startGame("rnbqkbnr/pppppppp/8/8/8/1p6/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b3",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/1p6/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Zwei Schritte nach vorne - nicht auf Startposition
	@Test
	public void Test1_10() {
		startGame("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",true);
		assertMove("d4-d6",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",true,false,false);
	}
	
	// Erreichen von Spielfeldende - Umwandlung in Dame
	@Test
	public void Test1_11() {
		startGame("rnb0bknr/pppPpppp/8/8/8/8/PPP1PPPP/RNBQKBNR/",true);
		assertMove("d7-d8",true,true);
		assertGameState("rnbQbknr/ppp1pppp/8/8/8/8/PPP1PPPP/RNBQKBNR/",false,false,false);
	}	
	
	// ================================= Schwarze Bauern Tests =====================================
	
	// Schritt vorw�rts, aber nicht am Zug
	@Test
	public void Test1_12() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b7-b6",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}

	// Einsetzen aus Reserve
	@Test
	public void Test1_13() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/PRp",false);
		assertMove("p-b4",false,true);
		assertGameState("rnbqkbnr/pppppppp/8/8/1p6/8/PPPPPPPP/RNBQKBNR/PR",true,false,false);
	}
	
	// Erreichen von Spielfeldende
	@Test
	public void Test1_14() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPp/RNBQKBN1/PRp",false);
		assertMove("h2-h1",false,true);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPP1/RNBQKBNq/PRp",true,false,false);
	}
	
	// Gegner schlagen (und Spielfeldende erreichen)
	@Test
	public void Test1_15() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPp/RNBQKBN1/PRp",false);
		assertMove("h2-g1",false,true);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPP1/RNBQKBq1/PRnp",true,false,false);
	}
	
	// Zwei Schritte vorw�rts von Startposition
	@Test
	public void Test1_16() {
		startGame("rnbqkbnr/pppppppp/8/8/1p6/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("b7-b5",false,true);
		assertGameState("rnbqkbnr/p1pppppp/8/1p6/1p6/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Zwei Schritte vorw�rts von Startposition - Figur auf Zielfeld
	@Test
	public void Test1_17() {
		startGame("rnbqkbnr/pppppppp/8/1r6/1p6/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("b7-b5",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/1r6/1p6/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Zwei Schritte vorw�rts von Startposition - Figur im Weg
	@Test
	public void Test1_18() {
		startGame("rnbqkbnr/pppppppp/1r6/8/1p6/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("b7-b5",false,false);
		assertGameState("rnbqkbnr/pppppppp/1r6/8/1p6/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Ein Schritt vorw�rts von Startposition - Figur auf Zielfeld
	@Test
	public void Test1_19() {
		startGame("rnbqkbnr/pppppppp/1r6/8/1p6/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("b7-b6",false,false);
		assertGameState("rnbqkbnr/pppppppp/1r6/8/1p6/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Zwei Schritte vorw�rts - nicht auf Startposition
	@Test
	public void Test1_20() {
		startGame("rnbqkbnr/p1pppppp/1pP5/1r6/1p6/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("b6-b4",false,false);
		assertGameState("rnbqkbnr/p1pppppp/1pP5/1r6/1p6/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// ============================= Turm Tests =======================================
	
	// Seitlich gehen
	@Test
	public void Test2_1() {
		startGame("rnbqkbnr/pppppppp/8/r7/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a5-h5",false,true);
		assertGameState("rnbqkbnr/pppppppp/8/7r/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Vorw�rts gehen
	@Test
	public void Test2_2() {
		startGame("rnbqkbnr/pppppppp/8/8/r7/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a4-a6",false,true);
		assertGameState("rnbqkbnr/pppppppp/r7/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Seitlich gehen - Figur im Weg
	@Test
	public void Test2_3() {
		startGame("rnbqkbnr/pppppppp/8/r5p1/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a5-h5",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/r5p1/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Seitlich gehen - Eigene Figur auf Zielpositioon
	@Test
	public void Test2_4() {
		startGame("rnbqkbnr/pppppppp/8/r5p1/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a5-g5",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/r5p1/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// L�ngs gehen - Figur im Weg
	@Test
	public void Test2_5() {
		startGame("rnbqkbnr/pppppppp/8/r5p1/n7/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a5-a3",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/r5p1/n7/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// L�ngs gehen - Eigene Figur auf Zielfeld
	@Test
	public void Test2_6() {
		startGame("rnbqkbnr/pppppppp/8/r5p1/n7/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a5-a4",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/r5p1/n7/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// L�ngs gehen - Gegner schlagen
	@Test
	public void Test2_7() {
		startGame("rnbqkbnr/pppppppp/8/r5p1/8/Q7/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a5-a3",false,true);
		assertGameState("rnbqkbnr/pppppppp/8/6p1/8/r7/PPPPPPPP/RNBQKBNR/q",true,false,false);
	}
	
	// Quer gehen - Gegner schlagen
	@Test
	public void Test2_8() {
		startGame("rnbqkbnr/pppppppp/8/r5Q1/8/Q7/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a5-g5",false,true);
		assertGameState("rnbqkbnr/pppppppp/8/6r1/8/Q7/PPPPPPPP/RNBQKBNR/q",true,false,false);
	}
	
	// ========================================= Damen Tests =====================================
	
	// Schr�g gehen und Bauern schlagen
	@Test
	public void Test3_1() {
		startGame("rnbqkbnr/pppppppp/8/1Q7/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b5-d7",true,true);
		assertGameState("rnbqkbnr/pppQpppp/8/8/8/8/PPPPPPPP/RNBQKBNR/P",false,false,false);
	}
	
	// ======================================= K�nig Tests ======================================
	
	// K�nig macht einen Schritt
	@Test
	public void Test4_1() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNB1KBNR/",true);
		assertMove("e1-d1",true,true);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBK1BNR/",false,false,false);
	}
	
	// ======================================= Incorrect Move-Strings ==============================================
	
	// falsche L�nge
	@Test
	public void Test10_1() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("-f6",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Richtige L�nge, aber Bindestrich an falschem Index
	@Test
	public void Test10_2() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("ar4-6",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Richtige L�nge, aber Bindestrich an falschem Index
	@Test
	public void Test10_3() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("af-6",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// falsches Symbol an Index 0
	@Test
	public void Test10_4() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("4r-f6",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// falsches Symbol an Index 1
	@Test
	public void Test10_5() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("ar-f6",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// falsches Symbol an Index 3
	@Test
	public void Test10_6() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a6-56",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// falsches Symbol an Index 4
	@Test
	public void Test10_7() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a6-fr",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Drop-Format - falsches Symbol an Index 0
	@Test
	public void Test10_8() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a-fr",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Drop-Format - falsches Symbol an Index 2
	@Test
	public void Test10_9() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("p-7r",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Drop-Format - falsches Symbol an Index 3
	@Test
	public void Test10_10() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("p-er",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// =================================== K�nigsbedrohung ==============================================
	
	// Bauer m�chte Gegner schlagen - K�nig w�re dann bedroht
	@Test
	public void Test11_1() {
		startGame("rnbqkbnr/pppppppp/5P2/8/8/8/PPPPRPPP/RNBQKBNR/",false);
		assertMove("e7-f6",false,false);
		assertGameState("rnbqkbnr/pppppppp/5P2/8/8/8/PPPPRPPP/RNBQKBNR/",false,false,false);
	}
	
	// Dame macht Move, sodass gegnerischer K�nig bedroht ist, K�nig kann nicht ausweichen und es kann nicht aus Reserve eingesetzt werden, aber es existieren Moves zum sch�tzen
	@Test
	public void Test11_2() {
		startGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPP1PPP/RNBQKBNR/",true);
		assertMove("d1-e2",true,true);
		assertGameState("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR/",false,false,false);
	}
	
	// Dame macht Move, sodass gegnerischer K�nig bedroht ist, K�nig kann nicht ausweichen, aber Reserve einsetzen
	@Test
	public void Test11_3() {
		startGame("rnbpknbr/pppp1ppp/8/8/8/8/PPPP1PPP/RNBQKBNR/p",true);
		assertMove("d1-e2",true,true);
		assertGameState("rnbpknbr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR/p",false,false,false);
	}
	
	// Dame setzt K�nig Schachmatt
	@Test
	public void Test11_4() {
		startGame("rnbpkrbr/pppp1ppp/8/8/8/8/PPPP1PPP/RNBQKBNR/",true);
		assertMove("d1-e2",true,true);
		assertGameState("rnbpkrbr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR/",false,true,true);
	}
	
	// Schachmatt - Reserve vorhanden aber kann nicht Droppen
	@Test
	public void Test11_5() {
		startGame("k6R/8/RR6/8/8/8/8/RNBQKBNR/p",true);
		assertMove("d1-e2",true,true);
		assertGameState("k6R/8/RR6/8/8/8/4Q3/RNB1KBNR/p",false,true,true);
	}
	
	// Schwarz setzt wei� Schachmatt (trotz vorhandener Reserve)
	@Test
	public void Test11_6() {
		startGame("K6r/8/rr6/8/8/8/p7/8/P",false);
		assertMove("a2-a1",false,true);
		assertGameState("K6r/8/rr6/8/8/8/8/q7/P",false,true,false);
	}
	
	// Wei� wird Schach gesetzt, kann Reserve Droppen um zu retten
	@Test
	public void Test11_7() {
		startGame("rnbpknbr/pppp1ppp/8/8/8/8/PPPP1PPP/RNBQKBNR/Pp",true);
		assertMove("d1-e2",true,true);
		assertGameState("rnbpknbr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR/Pp",false,false,false);
	}

	// Wei� wird Schachmatt gesetzt und hat keine Reserve
	@Test
	public void Test11_8() {
		startGame("RNBPKPBR/PPPP1PPP/8/8/8/8/pppp1ppp/rnbqkbnr/p",false);
		assertMove("d1-e2",false,true);
		assertGameState("RNBPKPBR/PPPP1PPP/8/8/8/8/ppppqppp/rnb1kbnr/p",false,true,false);
	}
	
	// K�nig wird bedroht - Einsetzversuch an Stelle, die Bedrohung nicht aufhebt
	@Test
	public void Test11_9() {
		startGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPRPPP/RNBQKBNR/p",false);
		assertMove("p-f3",false,false);
		assertGameState("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPRPPP/RNBQKBNR/p",false,false,false);
	}
	
	// ==================================== Drop Tests =========================================================
	
	// Drop auf leeres Feld
	@Test
	public void Test20_4() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/PRNBn",false);
		assertMove("n-f6",false,true);
		assertGameState("rnbqkbnr/pppppppp/5n2/8/8/8/PPPPPPPP/RNBQKBNR/PRNB",true,false,false);
	}
	
	// Bauer soll in erste Reihe eingesetzt werden
	@Test
	public void Test20_5() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBN1/PRNBp",false);
		assertMove("p-h1",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBN1/PRNBp",false,false,false);
	}
	
	// Bauer soll in letzte Reihe eingesetzt werden
	@Test
	public void Test20_6() {
		startGame("rnbqkbn1/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/PRNBp",false);
		assertMove("p-h8",false,false);
		assertGameState("rnbqkbn1/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/PRNBp",false,false,false);
	}
	
	// Drop auf eigene Figur
	@Test
	public void Test20_9() {
		startGame("rnbqkbnr/pppppppp/8/8/1R6/8/PPPPPPPP/RNBQKBNR/P",true);
		assertMove("P-b4",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/1R6/8/PPPPPPPP/RNBQKBNR/P",true,false,false);
	}
	
	// Drop auf gegnerische Figur
	@Test
	public void Test20_10() {
		startGame("rnbqkbnr/pppppppp/8/8/1r6/8/PPPPPPPP/RNBQKBNR/P",true);
		assertMove("P-b4",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/1r6/8/PPPPPPPP/RNBQKBNR/P",true,false,false);
	}
	
	// Drop ohne vorhandene Reserve
	@Test
	public void Test20_11() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("P-b4",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}

// ================================ Weiteres ==========================================
	// Zug von leerem Feld
	@Test
	public void Test30_1() {
		startGame("rnbqkbnr/pppppppp/8/8/1p6/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("a3-b4",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/1p6/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	// Spieler versucht generische Figur zu bewegen
	@Test
	public void Test30_2() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("a7-a6",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	@Test
	public void Test30_3() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false);
		assertMove("a2-a4",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",false,false,false);
	}
	
	// Spieler versucht, gegnerische Figur einzusetzen
	@Test
	public void Test30_4() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/p",true);
		assertMove("p-a6",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/p",true,false,false);
	}
	
	// WTF
	@Test
	public void Test30_5() {
		startGame("RnBqKbNr/ppPppP1p/5Pp1/8/8/8/PpPPPpPP/R1BQkBNR/QPnr",true);
		assertMove("c7-d8",true,true);
		assertGameState("RnBQKbNr/pp1ppP1p/5Pp1/8/8/8/PpPPPpPP/R1BQkBNR/QPQnr",false,false,false);
	}
}