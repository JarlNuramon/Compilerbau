package sdk.model;/*
					interface professorstuff.TokenList
					
					Praktikum Algorithmen und Datenstrukturen
					Beispiel zum Versuch 2
					
					Die Schnittstelle professorstuff.TokenList stellt die Konstanten f�r die
					Knotentypen eines Syntaxbaumes (Token) f�r Ziffernfolgen
					zur Verf�gung.
					*/

public interface TokenList {
	// Konstanten zur Bezeichnung der Knoten des Syntaxbaumes

	final byte NO_TYPE = 0, NUM = 1, DIGIT = 2, INPUT_SIGN = 3, EPSILON = 4,
			START = 5, NOT_FINAL = 6, KOMMA = 7, IDENT = 8, OPEN_PAR = 9,
			CLOSE_PAR = 10, PLUS = 11, MINUS = 12, MULT = 13, DIV = 14,
			EXPRESSION = 15, RIGHT_EXPRESSION = 16, TERM = 17, RIGHT_TERM = 18,
			OPERATOR = 20, PROGRAM = 21, FUNCTION = 22, FUN = 35,
			OPEN_METH = 23, CLOSE_METH = 24, EQUAL = 25, SEMICOLON = 26,
			EXPRESSION_LIST = 27, FUNCTION_CALL = 28, LET = 29, IF = 30,
			IF_STATEMENT = 46, WHILE = 31, WHILE_STATEMENT = 47,
			PARAMETER_LIST = 32, STATEMENT = 34, DEF_OPERATOR = 33,
			BOOL_OPERATOR = 49, BOOL_STATEMENT = 50, ASSERTION = 48;

	// Konstante, die angibt, dass die Semantische Funktion eines Knotens
	// undefiniert ist
	final int UNDEFINED = 0x10000001;

}