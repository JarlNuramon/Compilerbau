package ownCompiler;

import java.util.LinkedList;

public class SyntaxTree {

	List<SyntaxTree> children = new LinkedList<>();
	String value;
	Function function;
}
