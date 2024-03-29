package sdk.parser;

import java.util.Arrays;

import sdk.model.State;

/*
	NunScanner.java
	
	Diese Klasse implementiert die Zustnde und Transitionstabelle eines DEA f�r 
	Ziffernfolgen nach dem folgenden regul�ren Ausdruck:
	
													+
	NUM := {'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'|'0'}

*/

class SDKScanner extends Scanner {

	// -------------------------------------------------------------------------
	// Konstruktor (Legt die Zust�nde und Transitionstabelle des DEA an)
	// -------------------------------------------------------------------------

	SDKScanner() {
		State states[] = {State.START, State.KOMMA, State.IDENT, State.OPEN_PAR,
				State.CLOSE_PAR, State.PLUS, State.MINUS, State.MUL, State.DIV,
				State.DIGIT, State.FUN, State.OPEN_METH, State.CLOSE_METH,
				State.EQUAL, State.SEMICOLON, State.EXPRESSION_LIST,
				State.FUNCTION_CALL, State.EXPRESSION, State.TERM,
				State.RIGHT_TERM, State.OPERATOR, State.PROGRAM,
				State.RIGHT_EXPRESSION, State.LET, State.WHILE, State.IF,
				State.BOOL_OPERATOR};
		// Instanz des DEA anlegen
		this.dea = new Scanner.DEA(states);
		statesSorted = Arrays.stream(dea.states)
				.sorted((a1, a2) -> a2.getPrio() - a1.getPrio()).toList();
	}

	// Gibt den zum Zahlenwert passenden String des Tokentyps zurück
	// Implementierung der abstrakten Methode aus der Klasse professorstuff.Scanner
	public String getTokenString(State token) {
		return token.name();
	}

}