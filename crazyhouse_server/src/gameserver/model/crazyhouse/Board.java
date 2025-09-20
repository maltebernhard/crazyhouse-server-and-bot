package gameserver.model.crazyhouse;

public class Board {
	
	/*
	 * Die Klasse Board spiegelt den aktuellen Zustand des Spielbretts wieder.
	 * Dazu wird das Spielfeld als 8x8 Char-Array gespeichert, in dem die Figuren entsprechend ihrer Bezeichnung als Char gespeichert werden, '0' repr�sentiert ein leeres Feld.
	 * Auf diese Weise k�nnen Funktionen wie tryMove() mithilfe von Positionsindizes �bersichtlich implementiert werden.
	 * Die Reserve wird als String gespeichert, in welchem erst alle wei�en, dann alle schwarzen Figuren der Reserve stehen.
	 */
	
	protected char[][] board = new char[8][8];
	protected String reserve = "";
	
	/* =============================== CONSTRUCTOR ==================================== */
	
	// �bertr�gt Eingabestring in Character-Matrix und Reserve
	public Board(String str) {
		setBoard(str);
	}
	
	/* ================================ GET/SET ======================================== */
	
	// Generiert String aus Board-Matrix und Reserve
	public String getBoard() {
		String s = "";
		int counter = 0;
		for (int i=7; i>=0; i--) {
			for (int j=0; j<8; j++) {
				if (board[i][j] == '0') counter++;
				else if (counter>0) {
					s+=Integer.toString(counter);
					counter=0;	
					s+=board[i][j];
				}
				else s+=board[i][j];
			}
			if (counter>0) {
				s+=Integer.toString(counter);
				counter=0;
			}
			s+="/";
		}
		s+=reserve;
		return s;
	}
	
	// �bertr�gt Zustandsstring in Boardmatrix und Reserve
	public void setBoard(String str) {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				board[i][j]='0';
			}
		}
		reserve = "";
		int i=7;
		int j=0;
		for (char c : str.toCharArray()) {
			if (i==-1) {
				reserve+=Character.toString(c);
				continue;
			}
			if(c == '/') {
				i--;
				j=0;
			}
			else if ("123456789".indexOf(c)!=-1) j+=(c-'0');
			else {
				board[i][j]=c;
				j++;
			}
		}
	}
	
	public void printBoard() {
		String s="";
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				s+=board[i][j];
			}
			System.out.println(s);
			s="";
		}
	}
	
	// ==================================== tryMove() und wichtige Unterfunktionen =====================================
	
	// Unterscheidung zwischen Move und Drop und Ermittlung von Start und Ziel-Koordinaten des Zuges
	public boolean tryMove(String moveString, String player) {
		if (moveStringCorrect(moveString)) {
			int dashindex = moveString.indexOf('-');
			if (dashindex==1) {
				int tj = moveString.charAt(2)-'a';
				int ti = moveString.charAt(3)-'0'-1;
				String fig = moveString.substring(0,1);
				return tryPlaceFig(ti,tj,fig,player);
			}
			int sj = moveString.charAt(0)-'a';
			int si = moveString.charAt(1)-'0'-1;
			int tj = moveString.charAt(3)-'a';
			int ti = moveString.charAt(4)-'0'-1;
			return tryMoveFig(si,sj,ti,tj,player);
		}
		return false;
	}
	
	// �berpr�ft das korrekte Format eines moveStringa
	private boolean moveStringCorrect(String moveString) {
		int dashindex = moveString.indexOf('-');
		int length = moveString.length();
		if (length == 4 && dashindex == 1) {
			char[] c = moveString.toCharArray();
			return ("prnbqk".contains(Character.toString(c[0]).toLowerCase()) && ("abcdefgh".contains(Character.toString(c[2]))) && ("12345678".contains(Character.toString(c[3]))));
		}
		else if (length == 5 && dashindex == 2) {
			char[] c = moveString.toCharArray();
			return ("abcdefgh".contains(Character.toString(c[0])) && "12345678".contains(Character.toString(c[1])) && ("abcdefgh".contains(Character.toString(c[3]))) && ("12345678".contains(Character.toString(c[4]))));
		}
		return false;
	}
	
	// �berpr�fen der Bedingungen eines legalen Einsetz-Zuges (Figur in Reserve und K�nig nach Zug in Sicherheit, Bauern nicht in Zeilen 1 oder 8 einsetzbar)
	private boolean tryPlaceFig(int ti, int tj, String fig, String player) {
		if (reserve.contains(fig) && board[ti][tj]=='0' & ((player=="w" & "PRNBQK".contains(fig)) || (player=="b" & "prnbqk".contains(fig))) & !(fig.toLowerCase().charAt(0)=='p' & (ti==0 || ti==7))) {
			Board newboard = new Board(this.getBoard());
			newboard.placeFig(ti,tj,fig.charAt(0));				
			if (isKingSafe(newboard,player)) {
				placeFig(ti,tj,fig.charAt(0));
				return true;
			}
		}
		return false;
	}
	
	// Einsetzen einer Figur und L�schen aus der Reserve
	private void placeFig(int ti, int tj, char fig) {
		board[ti][tj]=fig;
		int i = reserve.indexOf(fig);
		reserve = reserve.substring(0,i) + reserve.substring(i+1);
	}
	
	// Bedingungen f�r legalen Move pr�fen (canMove() == true und K�nig nach Zug in Sicherheit)
	private boolean tryMoveFig(int si, int sj, int ti, int tj, String player) {
		if (canMove(si,sj,ti,tj)) {
			if ((player=="w" && "PRNBQK".contains(Character.toString(board[si][sj]))) || (player=="b" && "prnbqk".contains(Character.toString(board[si][sj])))) {
				Board newboard = new Board(this.getBoard());
				newboard.moveFig(si,sj,ti,tj,player);
				if (isKingSafe(newboard,player)) {
					moveFig(si,sj,ti,tj,player);
					return true;
				}
			}
		}
		return false;
	}
	
	// Move durchf�hren und ggfs. geschlagene Figuren umwandeln und zu Reserve hinzuf�gen
	private void moveFig(int si, int sj, int ti, int tj, String player) {
		if (player=="w" && board[ti][tj]!='0') {
			int i = 0;
			for (int ind=i; ind<reserveFindIndex(); ind++) if (reserve.charAt(ind)>Character.toUpperCase(board[ti][tj])) break; else i++;
			this.reserve = reserve.substring(0,i) + Character.toString(board[ti][tj]).toUpperCase() + reserve.substring(i);
		}
		else if (player!="w" && board[ti][tj]!='0') {
			int i = reserveFindIndex();
			for (int ind=i; ind<reserve.length(); ind++) if (reserve.charAt(ind)>Character.toLowerCase(board[ti][tj])) break; else i++;
			this.reserve = reserve.substring(0,i) + Character.toString(board[ti][tj]).toLowerCase() + reserve.substring(i);
		}
		board[ti][tj]=board[si][sj];
		board[si][sj]='0';
		if (ti==0 && board[ti][tj]=='p') {
			board[ti][tj]='q';
		}
		else if (ti==7 && board[ti][tj]=='P') {
			board[ti][tj]='Q';
		}
	}
	
	// ================================= MOVE BEDINGUNGSTESTS =========================================
	
	// Unterscheidet, welche Figur auf der Startposition steht und ruft entsprechende canMove_()-Unterfunktion auf
	private boolean canMove(int si, int sj, int ti, int tj) {
		char c = Character.toLowerCase(board[si][sj]);
		boolean res = false;
		if (c == 'p') {
			res = canMoveP(si,sj,ti,tj);
		}
		else if (c == 'r') {
			res = canMoveR(si,sj,ti,tj);
		}
		else if (c == 'n') {
			res = canMoveN(si,sj,ti,tj);
		}
		else if (c == 'b') {
			res = canMoveB(si,sj,ti,tj);
		}
		else if (c == 'q') {
			res = canMoveR(si,sj,ti,tj) || canMoveB(si,sj,ti,tj);
		}
		else if (c == 'k') {
			res = canMoveK(si,sj,ti,tj);
		}
		return res;
	}
	
	// Bauern: Einzelschritt, Doppelschritt von Startposition oder Schlagen eines Gegners
	private boolean canMoveP(int si, int sj, int ti, int tj) {
		boolean res;
		if (Character.isLowerCase(board[si][sj]) == false) {			
			res = (ti-si == 1 & tj == sj & board[ti][tj]=='0'); 														// Ein Schritt auf leeres Feld vorw�rts
			res = res || (ti-si == 2 && si==1 & tj == sj & board[ti][tj]=='0' & board[ti-1][tj]=='0'); 					// Doppelschritt von Startposition
			res = res || (ti-si==1 & Math.abs(tj-sj)==1 & "prnbqk".contains(Character.toString(board[ti][tj]))); 		// Gegenerische Figur wird schr�g geschlagen
		}
		else { 																											// Schwarze Bauern k�nnen sich nur in entgegengesetzte Richtung bewegen			
			res = (ti-si == -1 & tj == sj & board[ti][tj]=='0'); 														// Ein Schritt auf leeres Feld vorw�rts
			res = res || (ti-si == -2 && si==6 & tj == sj & board[ti][tj]=='0' && board[ti+1][tj]=='0'); 				// Doppelschritt von Startposition
			res = res || (ti-si == -1 & Math.abs(tj-sj)==1 & "PRNBQK".contains(Character.toString(board[ti][tj]))); 	// Gegenerische Figur wird schr�g geschlagen
		}
		return res;
	}
	
	// Turm: Bewegung nur in Zeilen/Spalten, niemand steht im Weg, keine eigene Figur auf Zielfeld
	private boolean canMoveR(int si, int sj, int ti, int tj) {
		boolean res = true;		
		if (si - ti != 0 && sj - tj == 0) {
			for (int i=1; i<Math.abs(ti-si); i++) {
				res = res && board[si+((ti-si)/(Math.abs(ti-si)))*i][sj]=='0';
			}
			
		}
		else if (si - ti == 0 && sj - tj != 0) {
			for (int j=1; j<Math.abs(tj-sj); j++) {
				res = res && board[si][sj+((tj-sj)/(Math.abs(tj-sj)))*j]=='0';
			}
		}
		else {
			return false;
		}
		return res && isEnemy(board[si][sj],board[ti][tj]);
	}
	
	// Springer: Bewegung nur in L-Form, keine eigene Figur im Zielfeld
	private boolean canMoveN(int si, int sj, int ti, int tj) {
		if ((Math.abs(si-ti)==1 && Math.abs(sj-tj)==2) || (Math.abs(si-ti)==2 && Math.abs(sj-tj)==1)) {
			return isEnemy(board[si][sj],board[ti][tj]);
		}
		return false;
	}
	
	// L�ufer: Nur Diagonale, niemand steht im Weg, keine eigene Figur auf Zielfeld
	private boolean canMoveB(int si, int sj, int ti, int tj) {
		boolean res = true;
		if (Math.abs(si-ti)==Math.abs(sj-tj)) {
			for (int i=1; i<Math.abs(si-ti); i++) {
				res = res && (board[si+((ti-si)/Math.abs(ti-si))*i][sj+((tj-sj)/Math.abs(tj-sj))*i]=='0');
			}
			return res && isEnemy(board[si][sj],board[ti][tj]);
		}
		return false;
	}
	
	// Dame: Turm oder Springer muss ziehen k�nnen
	
	// K�nig: Schrittweite in Spalten-/Zeilenrichtung maximal 1, keine eigene Figur auf Zielfeld
	private boolean canMoveK(int si, int sj, int ti, int tj) {
		if (Math.max(Math.abs(ti-si),Math.abs(tj-sj))>1) {
			return false;
		}
		else {
			return isEnemy(board[si][sj],board[ti][tj]);
		}
	}
	
	// ================================== K�nigssicherheit und �berpr�fen von Schachmatt ========================================
	
	// �berpr�ft f�r ein gegebenes Board und einen Spieler, ob dessen K�nig in Sicherheit steht
	private boolean isKingSafe(Board newboard, String player) {
		boolean res = true;
		int kc[] = wheresKing(newboard,player);
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (newboard.board[i][j]!='0' && Character.isLowerCase(newboard.board[i][j])==(player=="w")) {
					res = res && !newboard.canMove(i,j,kc[0],kc[1]);
				}
			}
		}
		return res;
	}
	
	/* 
	 * �berpr�ft, ob ein Schachmatt besteht, falls der K�nig des n�chten Spielers bedroht wird:
	 * 1. Kann der K�nig einen Zug machen, um auszuweichen?
	 * 2. Kann eine Figur aus der Reserve gedropt werden, um den K�nig zu retten?
	 * 3. Brute Force: Probiere alle m�glichen Z�ge und Pr�fe, ob K�nig sicher ist (enth�lt Funktionalit�t von 1., wird aber nur aufgerufen, wenn andere M�glichkeiten false liefern
	*/
	public boolean isGameOver(String player) {
		if (isKingSafe(this,player)) return false;
		else {
			return !(canKingDodge(player) || canDropToSave(player) || canDefendKing(player));
		}
	}
	
	// �ber�ft f�r alle Z�ge des K�nigs, ob dieser danach in Sicherheit steht
	private boolean canKingDodge(String player) {
		int kc[] = wheresKing(this,player);
		Board newboard = new Board(this.getBoard());
		for (int i=Math.max(-1,-kc[0]); i+kc[0]<=7; i++) {
			for (int j=Math.max(-1,-kc[1]); j+kc[1]<=7; j++) {
				if (canMove(kc[0],kc[1],kc[0]+i,kc[1]+j)) {
					newboard.moveFig(kc[0],kc[1],kc[0]+i,kc[1]+j,player);
					if (isKingSafe(newboard,player)) return true;
					newboard.setBoard(this.getBoard());
				}
			}
		}
		return false;
	}
	
	// �berpr�ft f�r alle Felder, ob nach einem Drop der K�nig in Sicherheit steht
	private boolean canDropToSave(String player) {
		char c=findReserve(player);
		if (c!='0') {
			Board newboard = new Board(this.getBoard());
			for (int i=0; i<=7; i++) {
				for (int j=0; j<=7; j++) {
					if (newboard.tryPlaceFig(i,j,Character.toString(c),player)) return true;
					newboard.setBoard(getBoard());
				}
			}
		}
		return false;
	}
	
	// Iteriert �ber alle Felder und ruft f�r eigene Figuren canSaveing() auf
	private boolean canDefendKing(String player) {
		for (int i=0; i<=7; i++) {
			for (int j=0; j<=7; j++) {
				if ((player=="w" && "PRNBQK".contains(Character.toString(board[i][j]))) || (player=="b" && "prnbqk".contains(Character.toString(board[i][j])))) {
					if (canSaveKing(i,j,player)) return true;
				}
			}
		}
		return false;
	}
	
	// Pr�ft f�r alle m�glichen Z�ge einer Figur, ob es einen Zug gibt, nach dem der K�nig in Sicherheit ist
	private boolean canSaveKing(int si, int sj, String player) {
		Board newboard = new Board(this.getBoard());
		for (int i=0; i<=7; i++) {
			for (int j=0; j<=7; j++) {
				if (canMove(si,sj,i,j)) {
					newboard.moveFig(si,sj,i,j,player);
					if (isKingSafe(newboard,player)) return true;
					newboard.setBoard(getBoard());
				}
			}
		}
		return false;
	}
	
	// ====================================== HILFSFUNKTIONEN =======================================
	
	// Gibt f�r einen char s true zur�ck, wenn es sich bei char t um eine gegnerische Figur oder ein leeres Feld ('0') handelt
	private boolean isEnemy(char s, char t) {
		if (Character.isLowerCase(s) == false) {
			return ("0prnbqk".contains(Character.toString(t)));
		}
		else {
			return ("0PRNBQK".contains(Character.toString(t)));
		}
	}
	
	// Gibt den Index der ersten schwarzen Figur der Reserve zur�ck - an dieser Stelle (also in der Mitte) werden neue Figuren hinzugef�gt 
	private int reserveFindIndex() {
		int i = 0;
		while (i<reserve.length() && !Character.isLowerCase(reserve.charAt(i))) {
			i++;
		}
		return i;
	}
	
	// Gibt f�r ein Board und einen Spieler die Indizes der K�nigsposition des Spielers zur�ck
	private int[] wheresKing(Board newboard, String player) {
		int indices[] = new int[2];
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (newboard.board[i][j]=='K' && player=="w") {
					indices[0]=i;
					indices[1]=j;
				}
			}
		}
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (newboard.board[i][j]=='k' && player=="b") {
					indices[0]=i;
					indices[1]=j;
				}
			}
		}
		return indices;
	}
	
	// Findet den Index der ersten schwarzen Figur im Reservestring (hier werden neue Figuren beider Farben hinzugef�gt)
	private char findReserve(String player) {
		char c[] = reserve.toCharArray();
		char res ='0';
		if (player=="w") {
			for (int i = 0; i < c.length; i++) {
				if (Character.isUpperCase(c[i])) res = c[i];
				if (res != 'P') return res;
			}
		}
		else {
			for (int i = 0; i < c.length; i++) {
				if (Character.isLowerCase(c[i])) res = c[i];
				if (res != 'p') return res;
			}
		}
		return res;
	}
}

