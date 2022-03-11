package ownCompiler;

import java.util.LinkedList;
import java.util.List;

public class SyntaxTree {

	List<SyntaxTree> children = new LinkedList<>();
	String value;
	Function function;
}
