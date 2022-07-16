package ownCompiler;

public class ParserApplication {

    public static void main(String args[]) {

        SyntaxTree parseTree = new SyntaxTree(TokenList.PROGRAM);

        SDKParser parser = new SDKParser(parseTree);

        SDKCodeGen codeGenerierer = new SDKCodeGen();

        // Einlesen der Datei
        if (parser.readInput("testdatei_arithmetik.txt"))
            if (parser.lexicalAnalysis()) {
                parser.updatePointer();
                if (parser.program(parseTree) && parser.inputEmpty()) {
                    parseTree.printSyntaxTree(0);
                    codeGenerierer.generateCode(parseTree);
                } else {
                    System.out.println("Fehler im Ausdruck");

                    parseTree.printSyntaxTree(0);
                }
            } else
                System.out.println("Fehler in lexikalischer Analyse");
    }// main
}
