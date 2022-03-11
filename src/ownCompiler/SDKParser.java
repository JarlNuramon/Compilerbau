package ownCompiler;

import java.text.ParseException;
import java.util.List;

public class SDKParser {

/**
 * program -> function program | function
	function -> ident openPar parameterList closePar expression
	parameterList -> ident komma parameterList | ident | Epsilon 
	expression -> term rightExpression
	rightExpression -> plus term rightExpression 
	rightExpression -> minus term rightExpression 
	rightExpression -> Epsilon
	term -> operator rightTerm
	rightTerm -> mult operator rightTerm 
	rightTerm -> div operator rightTerm 
	rightTerm -> Epsilon
	operator -> openPar expression closePar | num | ident | functionCall
	functionCall -> ident openPar expressionList closePar
	expressionList -> expression komma expressionList | expression | Epsilon
 * @param lines
 * @throws ParseException 
 */
	public void parseFile(List<String> lines) throws ParseException {
		SyntaxTree root = new SyntaxTree(Function.PROGRAMM);
		
		if(parseProgramm(root,lines))
			throw new ParseException("ERROR", 0);
	}

   private boolean parseProgramm(SyntaxTree root, List<String> lines) {
	// TODO Auto-generated method stub
	
}

}
