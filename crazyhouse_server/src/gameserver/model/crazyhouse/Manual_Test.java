package gameserver.model.crazyhouse;

public class Manual_Test {

	public static void main(String[] args) {
		Board board = new Board("rnbq3r/pppp1kPp/8/8/1P6/8/PP2PnPP/RNBQKBNR/BPPpp");
		
		board.tryMove("g7-h8","w");
	    
		System.out.println(board.getBoard());
		System.out.println("rnbq3Q/pppp1k1p/8/8/1P6/8/PP2PnPP/RNBQKBNR/BPPRpp");		
	}
}
