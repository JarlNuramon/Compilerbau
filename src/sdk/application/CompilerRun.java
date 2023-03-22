package sdk.application;

import java.util.Collections;
import java.util.List;

import sdk.codegen.SDKCodeGen;
import sdk.model.SyntaxTree;
import sdk.model.TokenList;
import sdk.parser.SDKParser;

public class CompilerRun {

	public static List<String> getPseudoCodeOrError(String[] args) {
		SyntaxTree parseTree = new SyntaxTree(TokenList.PROGRAM);
		SDKParser parser = new SDKParser(parseTree);
		SDKCodeGen codeGenerierer = new SDKCodeGen();
		// Einlesen der Datei
		if (parser.readInput(args[0].toString()))
			if (parser.lexicalAnalysis()) {
				parser.updatePointer();
				if (parser.program(parseTree) && parser.inputEmpty()) {
					System.out.println();
					parseTree.printSyntaxTree(0);
					System.out.println();
					return codeGenerierer.generatePseudoCode(parseTree);
				} else {
					System.out.println("Fehler im Ausdruck");
					parseTree.printSyntaxTree(0);
				}
			} else
				System.out.println("Fehler in lexikalischer Analyse");
		return Collections.emptyList();
	}
}
