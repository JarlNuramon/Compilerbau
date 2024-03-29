package sdk.model;/*
					class professorstuff.SyntaxTree
					
					Praktikum Algorithmen und Datenstrukturen
					Grundlage zum Versuch 2
					
					professorstuff.SyntaxTree beschreibt die Knoten eines Syntaxbaumes
					mit den Methoden zum Aufbau des Baums.
					*/

import java.util.LinkedList;
import java.util.Objects;

public class SyntaxTree implements TokenList {
	// Attribute

	// value enth�lt die semantsiche Funktion des Teilbaums
	// mit Wurzelknoten this
	public Semantic value;
	// linker bzw. rechter Teilbaum (null bei Bl�ttern), rightNode=null,
	// wenn professorstuff.Operator nur einen Operanden hat
	private LinkedList<SyntaxTree> childNodes;
	// Art des Knotens gem�� der Beschreibung in der Schnittstelle Arithmetic
	private byte token;
	// Zeichen des Knotens, falls es sich um einen Bl�tterknoten, der ein
	// Eingabezeichen repr�sentiert, handelt, d.h. einen Knoten mit dem Token
	// DIGIT oder MATH_SIGN.
	private String character;

	// -------------------------------------------------------------------------
	// Konstruktor des Syntaxbaumes
	// -------------------------------------------------------------------------

	// Der Konstruktor bekommt den TokenTyp t des Knotens �bergeben
	public SyntaxTree(byte t) {
		this.childNodes = new LinkedList<>();
		character = "";
		setToken(t);
		setSemantikFunction(t);
	}
	// -------------------------------------------------------------------------
	// get und set Methoden des Syntaxbaumes
	// -------------------------------------------------------------------------

	// Gibt den aktuellen Konten des Syntaxbaumes zur�ck
	public byte getToken() {
		return this.token;
	}

	// Setzt den Typ des Tokens auf den �bergabeparameter t
	// Zu den m�glichen TokenTypen siehe Interface TokenList.java
	public void setToken(byte t) {
		this.token = t;
	}

	// Gibt das zum Knoten geh�rende Eingabezeichen zur�ck
	public String getCharacter() {
		return this.character;
	}

	// Bei einem Knoten, der ein Eingabezeichen repr�sentiert (INPUT_SIGN)
	// wird mit dieser Methode das Zeichen im Knoten gespeichert
	public void setCharacter(String character) {
		this.character = character;
	}

	// Gibt den Syntaxbaum mit entsprechenden Einr�ckungen auf der Konsole
	// aus.
	public void printSyntaxTree(int t) {
		for (int i = 0; i < t; i++)
			System.out.print("  ");
		System.out.print(this.getTokenString());
		if (!Objects.equals(this.character, ""))
			System.out.println(": " + this.getCharacter());
		else
			System.out.println("");
		for (SyntaxTree childNode : this.childNodes) {
			childNode.printSyntaxTree(t + 1);
		}
	}

	// Gibt den zum Zahlenwert passenden String des Tokentyps zur�ck
	public String getTokenString() {
		return switch (this.token) {
			case 0 -> "NO_TYPE";
			case 9 -> "OPEN_PAR";
			case 10 -> "CLOSE_PAR";
			case 15 -> "EXPRESSION";
			case 16 -> "RIGHT_EXPRESSION";
			case 17 -> "TERM";
			case 18 -> "RIGHT_TERM";
			case 1 -> "NUMBER";
			case 20 -> "OPERATOR";
			case 7 -> "KOMMA";
			case 3 -> "INPUT_SIGN";
			case 4 -> "EPSILON";
			case 11 -> "PLUS";
			case 12 -> "MINUS";
			case 13 -> "MULT";
			case 14 -> "DIV";
			case 8 -> "IDENT";
			case 5 -> "START";
			case 21 -> "PROGRAM";
			case 22 -> "FUNCTION";
			case 23 -> "OPEN_METH";
			case 24 -> "CLOSE_METH";
			case 25 -> "EQUAL";
			case 29 -> "LET";
			case 32 -> "PARAMETER_LIST";
			case 33 -> "DEF_OPERATOR";
			case 28 -> "FUNCTION_CALL";
			case 27 -> "EXPRESSION_LIST";
			case 26 -> "SEMICOLON";
			case 34 -> "STATEMENT";
			case 35 -> "FUN";
			case 30 -> "IF";
			case 31 -> "WHILE";
			case 47 -> "WHILE-STATEMENT";
			case 46 -> "IF-STATEMENT";
			case 48 -> "ASSERTION";
			case 49 -> "BOOL_OPERATOR";
			case 50 -> "BOOL_STATEMENT";
			case 2 -> "DIGIT";
			default -> this.token + "";
		};
	}

	// Bestimmt und speichert die semantsiche Funktion des Kontens in
	// Abh�ngigkeit vom Knotentyp
	void setSemantikFunction(byte b) {
		switch (b) {/*
					* case 1: value=new professorstuff.Expression(); break; case 2: value=new
					* professorstuff.RightExpression(); break; case 3: value=new
					* professorstuff.Term(); break; case 4: value=new professorstuff.RightTerm();
					* break; case 5: value=new professorstuff.Num(); break; case 6: value=new
					* professorstuff.Operator(); break; case 7: value=new Digit(); break;
					*/
			default -> value = new Semantic();
		}
	}

	// Legt einen neuen Teilbaum als Kind des aktuellen Knotens an und gibt die
	// Referenz auf seine Wurzel zur�ck
	public SyntaxTree insertSubtree(byte b) {
		SyntaxTree node;
		node = new SyntaxTree(b);
		this.childNodes.addLast(node);
		return node;
	}

	// Gibt die Refernz der Wurzel des i-ten Kindes des aktuellen
	// Knotens zur�ck
	public SyntaxTree getChild(int i) {
		if (i > this.childNodes.size())
			return null;
		else
			return this.childNodes.get(i);
	}

	// Gibt die Referenz auf die Liste der Kinder des aktuellen Knotens zur�ck
	public LinkedList<SyntaxTree> getChildNodes() {
		return this.childNodes;
	}

	// Gibt die Zahl der Kinder des aktuellen Konotens zur�ck
	public int getChildNumber() {
		return childNodes.size();
	}

}