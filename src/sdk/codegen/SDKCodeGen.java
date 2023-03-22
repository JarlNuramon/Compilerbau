package sdk.codegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import sdk.model.PseudoCodeConstants;
import sdk.model.SyntaxTree;
import sdk.model.TokenList;

/**
 * @author Narwutsch Dominic created on 16.07.2022
 */
public class SDKCodeGen implements TokenList {

	private final List<String> code = new ArrayList<>();
	private final Map<String, String> refs = new LinkedHashMap<>();
	private final Map<String, String> functionRefs = new HashMap<>();
	private final Map<String, List<String>> params = new HashMap<>();
	private int labelCounter = 1;

	public List<String> generatePseudoCode(SyntaxTree syntaxTree) {
		HashMap<String, String> scope = new HashMap<>();
		addLibrary();
		generateCode(syntaxTree, scope);
		code.add(PseudoCodeConstants.HALT);
		return code;
	}

	private void addLibrary() {
		labelCounter++;
		functionRefs.put("log", "label" + refs.size());
		refs.put("label" + refs.size(), "L1");
		params.put("log", List.of("label" + refs.size()));
		code.add("GOTO E1");
		code.add("LABEL L1");
		code.add("STACKTOP 1.idplace");
		code.add("LOAD 1.idplace.value.value");
		code.add("OUT");
		code.add("STORE 1.idplace.value");
		code.add("POP R1");
		code.add("MOVE R1");
		code.add(PseudoCodeConstants.GOTOSTACK);
		code.add("LABEL E1");
		code.add("");
	}

	private void generateCode(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		if (syntaxTree.getToken() == EPSILON)
			return;

		List<SyntaxTree> child = new ArrayList<>();
		switch (syntaxTree.getToken()) {
			case FUNCTION -> function(syntaxTree, new HashMap<>(scope));
			case EXPRESSION -> code
					.addAll(genExpressionCode(syntaxTree, scope, null));
			case WHILE_STATEMENT -> code
					.addAll(whileStatement(syntaxTree, new HashMap<>(scope)));
			case IF_STATEMENT -> code
					.addAll(ifStatement(syntaxTree, new HashMap<>(scope)));
			default -> child = syntaxTree.getChildNodes();
		}

		for (SyntaxTree tree : child)
			generateCode(tree, scope);
	}

	private Collection<String> IfWhileStatement(SyntaxTree syntaxTree,
			HashMap<String, String> scope, String outLabel) {
		LinkedList<String> boolStmnt = new LinkedList<>();

		for (SyntaxTree child : syntaxTree.getChildNodes()) {
			switch (child.getToken()) {
				case EXPRESSION -> {
					boolStmnt.addAll(boolStatement(syntaxTree, scope));
					boolStmnt.add(PseudoCodeConstants.GOFALSE + " " + outLabel);
				}
				case STATEMENT -> {
					if (!child.getChildNodes().isEmpty()) {
						boolStmnt.addAll(genExpressionCode(
								child.getChildNodes().get(0), scope, null));
					}
				}
			}
		}

		return boolStmnt;
	}

	private Collection<String> ifStatement(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		String outLabel = "L" + labelCounter++;

		LinkedList<String> ifStmnt = new LinkedList<>(
				IfWhileStatement(syntaxTree, scope, outLabel));

		ifStmnt.add(PseudoCodeConstants.LABEL + " " + outLabel);

		return ifStmnt;
	}

	private LinkedList<String> whileStatement(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		LinkedList<String> whileStmnt = new LinkedList<>();

		String label = "L" + labelCounter++;
		whileStmnt.add(PseudoCodeConstants.LABEL + " " + label);

		String outLabel = "L" + labelCounter++;

		whileStmnt.addAll(IfWhileStatement(syntaxTree, scope, outLabel));

		whileStmnt.add(PseudoCodeConstants.GOTO + " " + label);
		whileStmnt.add(PseudoCodeConstants.LABEL + " " + outLabel);

		return whileStmnt;
	}

	private Collection<String> boolStatement(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		List<String> boolStmnt = new LinkedList<>();
		List<SyntaxTree> childs = syntaxTree.getChildNodes();
		if (syntaxTree.getToken() == BOOL_STATEMENT) {
			String ident = null;
			String operator = null;
			for (SyntaxTree child : childs)
				switch (child.getToken()) {
					case TERM -> boolStmnt.addAll(genExpressionCode(
							syntaxTree.getChild(2), scope, null));
					case IDENT -> ident = PseudoCodeConstants.LOAD + " "
							+ posInRefs(scope.get(child.getCharacter()))
							+ ".idplace.value.value";
					case BOOL_OPERATOR -> operator = switch (child
							.getCharacter()) {
							case "==" -> PseudoCodeConstants.EQUAL_PSEUD;
							case "<" -> PseudoCodeConstants.LESS_PSEUD;
							case ">" -> PseudoCodeConstants.GREATER_PSEUD;
							case "<=" -> PseudoCodeConstants.LESS_EQUAL_PSEUD;
							case ">=" -> PseudoCodeConstants.GREATER_EQUAL_PSEUD;
							case "!=" -> PseudoCodeConstants.NOT_EQUAL_PSEUD;
							default -> "";
						};

				}
			boolStmnt.add(ident);
			boolStmnt.add(operator);
			return boolStmnt;
		}
		for (SyntaxTree child : childs)
			boolStmnt.addAll(boolStatement(child, scope));
		return boolStmnt;
	}

	private void functionCall(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		LinkedList<String> funCall = new LinkedList<>();
		String functionName = "";

		String label = "L" + labelCounter++;
		funCall.add(PseudoCodeConstants.LABEL + " " + label);

		functionName = createPostfixAnnotitation(syntaxTree, scope, funCall,
				functionName);

		for (String param : reverseList(params.get(functionName))) {
			funCall.add(PseudoCodeConstants.LOAD + " " + posInRefs(param)
					+ ".idplace.value");
		}

		funCall.add(PseudoCodeConstants.MOVE + " " + label);
		code.addAll(reverseList(funCall));
	}

	private String createPostfixAnnotitation(SyntaxTree syntaxTree,
			HashMap<String, String> scope, LinkedList<String> funCall,
			String functionName) {
		for (SyntaxTree child : syntaxTree.getChildNodes())
			switch (child.getToken()) {
				case IDENT -> {
					functionName = child.getCharacter();
					String fun = functionRefs.get(child.getCharacter());
					funCall.add(PseudoCodeConstants.GOTO + " " + refs.get(fun));
				}
				case EXPRESSION_LIST -> funCall
						.addAll(reverseList(expressionList(child, scope)));
			}
		return functionName;
	}

	private List<String> expressionList(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		List<String> expressionListCode = new ArrayList<>();

		for (SyntaxTree child : syntaxTree.getChildNodes())
			switch (syntaxTree.getToken()) {
				case EXPRESSION -> expressionListCode
						.addAll(genExpressionCode(child, scope, null));
				case EXPRESSION_LIST -> expressionListCode
						.addAll(expressionList(child, scope));
			}
		return expressionListCode;
	}

	private int posInRefs(String character) {
		if (refs.containsKey(character)) {
			int pos = 0;
			for (String key : refs.keySet()) {
				if (character == null)
					throw new RuntimeException("Unbekannte Variable");
				if (character.equals(key))
					return pos;
				pos++;
			}
		} else {
			refs.put(character, null);
		}
		return refs.size() - 1;
	}

	private void function(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		String functionName = "";
		AtomicReference<String> lastEquation = new AtomicReference<>(null);

		String label = "L" + labelCounter++;
		code.add(PseudoCodeConstants.GOTO + " E" + label.substring(1));
		code.add(PseudoCodeConstants.LABEL + " " + label);

		Map<String, String> parameterList = new HashMap<>();
		for (SyntaxTree tree : syntaxTree.getChildNodes())
			switch (tree.getToken()) {
				case IDENT -> {
					functionName = tree.getCharacter();
					functionRefs.put(functionName, "label" + refs.size());
					refs.put("label" + refs.size(), label);
					params.put(functionName, new ArrayList<>());
				}
				case PARAMETER_LIST -> {
					parameterList = parameterList(tree, scope);
					for (String parameter : reverseList(parameterList.values()
							.stream().sorted().toList())) {
						code.add(PseudoCodeConstants.STACKTOP + " "
								+ posInRefs(parameter) + ".idplace");
						params.get(functionName).add(parameter);
					}
				}
				case CLOSE_METH -> {
					for (String parameter : reverseList(parameterList.values()
							.stream().sorted().toList())) {
						code.add(PseudoCodeConstants.STORE + " "
								+ posInRefs(parameter) + ".idplace.value");
					}
					String labelNumber = label.substring(1);
					code.add(PseudoCodeConstants.POP + " R" + labelNumber);
					code.add(PseudoCodeConstants.LOAD + " "
							+ posInRefs(lastEquation.get()) + ".idplace.value");
					code.add(PseudoCodeConstants.MOVE + " R" + labelNumber);
					code.add(PseudoCodeConstants.GOTOSTACK);
					code.add(PseudoCodeConstants.LABEL + " E" + labelNumber);
					code.add("");
				}
				case STATEMENT -> {
					if (!tree.getChildNodes().isEmpty())
						code.addAll(
								genExpressionCode(tree.getChildNodes().get(0),
										scope, lastEquation::set));
				}
				case WHILE_STATEMENT -> code
						.addAll(whileStatement(tree, new HashMap<>(scope)));
				case IF_STATEMENT -> code
						.addAll(ifStatement(tree, new HashMap<>(scope)));
			}
	}

	private List<String> genExpressionCode(SyntaxTree syntaxTree,
			HashMap<String, String> scope, Consumer<String> o) {
		List<String> expressionCode = new ArrayList<>();
		String lastIdent = "";
		for (SyntaxTree tree : expression(syntaxTree, scope))
			switch (tree.getToken()) {
				case IDENT -> {
					lastIdent = tree.getCharacter();
					String par = scope.get(tree.getCharacter());
					int ref = posInRefs(par);
					expressionCode.add(PseudoCodeConstants.LOAD + " " + ref
							+ ".idplace.value.value");
				}
				case EQUAL -> {
					expressionCode.remove(expressionCode.size() - 1);
					String par = scope.get(lastIdent);
					int ref = posInRefs(par);
					expressionCode.add(PseudoCodeConstants.STORE + " " + ref
							+ ".idplace.value");
					if (o != null)
						o.accept(par);
				}
				case MULT -> expressionCode.add(PseudoCodeConstants.MUL_PSEUD);
				case DIV -> expressionCode.add(PseudoCodeConstants.DIV_PSEUD);
				case PLUS -> expressionCode.add(PseudoCodeConstants.ADD_PSEUD);
				case MINUS -> expressionCode.add(PseudoCodeConstants.SUB_PSEUD);
				case DIGIT -> expressionCode.add(
						PseudoCodeConstants.LOAD + " " + tree.getCharacter());
			}
		return expressionCode;
	}

	private Map<String, String> parameterList(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		Map<String, String> list = new HashMap<>();

		byte token = syntaxTree.getToken();
		if (token == LET || token == KOMMA || token == EPSILON)
			return list;
		else if (token == IDENT) {
			String ident = "label" + refs.size();
			scope.put(syntaxTree.getCharacter(), ident);
			refs.put(ident, null);
			list.put(syntaxTree.getCharacter(), ident);
		}

		List<SyntaxTree> child = syntaxTree.getChildNodes();
		for (SyntaxTree tree : child)
			list.putAll(parameterList(tree, scope));

		return list;
	}

	private List<SyntaxTree> expression(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		List<SyntaxTree> list = new ArrayList<>();

		List<SyntaxTree> child = syntaxTree.getChildNodes();
		for (SyntaxTree tree : child)
			list.addAll(term(tree, scope));

		for (int i = 0; i < list.size() - 1; i++) {
			String item = list.get(i).getCharacter();
			if (item.equals("*") || item.equals("/") || item.equals("+")
					|| item.equals("-"))
				Collections.swap(list, i, ++i);
		}

		List<String> charList = list.stream().map(SyntaxTree::getCharacter)
				.toList();
		if (charList.contains("=")) {
			int index = charList.indexOf("=");
			SyntaxTree assign = list.get(index);
			list.remove(index);
			SyntaxTree ident = list.get(index);
			list.remove(index);
			list.add(ident);
			list.add(assign);
		}

		return list;
	}

	private List<SyntaxTree> term(SyntaxTree syntaxTree,
			HashMap<String, String> scope) {
		List<SyntaxTree> list = new ArrayList<>();

		byte token = syntaxTree.getToken();
		if (token == EPSILON || token == LET)
			return list;
		else if (token == DEF_OPERATOR) {
			list.addAll(createSymbol(syntaxTree, scope));
			return list;
		} else if (token == FUNCTION_CALL) {
			functionCall(syntaxTree, scope);
			return list;
		} else if (!syntaxTree.getCharacter().isEmpty())
			list.add(syntaxTree);

		List<SyntaxTree> child = reverseList(syntaxTree.getChildNodes());
		for (SyntaxTree tree : child)
			list.addAll(term(tree, scope));

		return list;
	}

	private List<SyntaxTree> createSymbol(SyntaxTree tree,
			HashMap<String, String> scope) {
		List<SyntaxTree> list = new ArrayList<>();

		if (tree.getToken() == IDENT) {
			String label = "label" + refs.size();
			scope.put(tree.getCharacter(), label);
			refs.put(label, null);
			list.add(tree);
		}

		for (SyntaxTree chlid : tree.getChildNodes())
			list.addAll(createSymbol(chlid, scope));
		return list;
	}

	private <T> List<T> reverseList(Collection<T> list) {
		List<T> reverseList = new ArrayList<>(list);
		Collections.reverse(reverseList);
		return reverseList;
	}
}
