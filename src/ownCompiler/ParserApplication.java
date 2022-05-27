package ownCompiler;

public class ParserApplication {

    public static void main(String args[]) {

	SyntaxTree parseTree = new SyntaxTree(TokenList.PROGRAM);

	SDKParser parser = new SDKParser(parseTree);

	// Einlesen der Datei
	if (parser.readInput("testdatei_arithmetik.txt"))
	    if (parser.lexicalAnalysis())
		if (parser.expression(parseTree) && parser.inputEmpty()) {
		    parseTree.printSyntaxTree(0);
		} else
		    System.out.println("Fehler im Ausdruck");
	    else
		System.out.println("Fehler in lexikalischer Analyse");
    }// main
}
