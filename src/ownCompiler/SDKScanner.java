package ownCompiler;

import professorstuff.Scanner;

/*
	NunScanner.java
	
	Diese Klasse implementiert die Zustnde und Transitionstabelle eines DEA f�r 
	Ziffernfolgen nach dem folgenden regul�ren Ausdruck:
	
													+
	NUM := {'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'|'0'}

*/


class SDKScanner extends Scanner {

    //-------------------------------------------------------------------------
    // Konstruktor (Legt die Zust�nde und Transitionstabelle des DEA an)
    //-------------------------------------------------------------------------

    SDKScanner() {
        // Transitionstabelle zum regulären Ausdruck
        //	    											+
        // NUM := {'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'|'0'}

        //<editor-fold desc="Description">
        String transitions[][][] = {
                //						START	KOMMA			IDENT																							        OPEN_PAR	  CLOSE_PAR		PLUS	MINUS	  MUL	    DIV									NUM              	FUNCTION  OPEN_METH  CLOSE_METH  EQUAL  SEMICOLON  EXPRESSION_LIST  FUNCTION_CALL  EXPRESSION  TERM  RIGHT_TERM  OPERATOR  PROGRAM  RIGHT_EXPRESSEN
                //				------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                /*START*/                {{}, {","}, {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"}, {"("}, {")"}, {"+"}, {"-"}, {"*"}, {"/"}, {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}, {"fun"}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*KOMMA*/                {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*IDENT*/                {{}, {}, {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {"="}, {";"}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*OPEN_PAR*/            {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*CLOSE_PAR*/            {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {"{"}, {"}"}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*PLUS*/                {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*MINUS*/                {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*MUL*/                    {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*DIV*/                    {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*NUM  */                {{}, {}, {}, {}, {}, {}, {}, {}, {}, {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*FUNCTION*/            {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {"{"}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*OPEN_METH*/        {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*CLOSE_METH*/        {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*EQUAL*/            {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*SEMICOLON*/        {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {"}"}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*EXPRESSION_LIST*/        {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*FUNCTION_CALL*/    {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*EXPRESSION*/        {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*TERM*/                {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*RIGHT_TERM*/        {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*OPERATOR*/            {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*PROGRAM*/            {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
                /*RIGHT_EXPRESSION*/    {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}}};
        //				-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //</editor-fold>
        // Zustände zum DEA
        State states[] = {State.START, State.KOMMA, State.IDENT, State.OPEN_PAR, State.CLOSE_PAR, State.PLUS, State.MINUS, State.MUL, State.DIV, State.NUM, State.FUNCTION, State.OPEN_METH, State.CLOSE_METH, State.EQUAL, State.SEMICOLON, State.EXPRESSION_LIST, State.FUNCTION_CALL, State.EXPRESSION, State.TERM, State.RIGHT_TERM, State.OPERATOR, State.PROGRAM, State.RIGHT_EXPRESSION};
        // Instanz des DEA anlegen
        this.dea = new Scanner.DEA(states);
    }

    // Gibt den zum Zahlenwert passenden String des Tokentyps zurück
    // Implementierung der abstrakten Methode aus der Klasse professorstuff.Scanner
    public String getTokenString(State token) {
        return token.name();
    }


}