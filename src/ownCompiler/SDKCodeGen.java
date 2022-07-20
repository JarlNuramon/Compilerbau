package ownCompiler;

import java.util.*;

/**
 * @author Narwutsch Dominic
 * created on 16.07.2022
 */
public class SDKCodeGen {
    private final List<String> code = new ArrayList<>();
    private final Map<String, String> refs = new LinkedHashMap<>();
    private int labelCounter = 1;

    List<String> generateCode(SyntaxTree syntaxTree) {
        _generateCode(syntaxTree);
        code.add("HALT");
        return code;
    }

    private void _generateCode(SyntaxTree syntaxTree) {
        if (syntaxTree.getTokenString().equals("EPSILON")) return;

        List<SyntaxTree> child = new ArrayList<>();
        switch (syntaxTree.getTokenString()) {
            case "FUNCTION" -> function(syntaxTree);
            case "EXPRESSION" -> code.addAll(genExpressionCode(syntaxTree));
            default -> child = syntaxTree.getChildNodes();
        }

        for (SyntaxTree tree : child)
            _generateCode(tree);
    }

    private void functionCall(SyntaxTree syntaxTree) {
        LinkedList<String> funCall = new LinkedList<>();

        String label = "L" + labelCounter++;
        funCall.add("LABEL " + label);

        for (SyntaxTree child : syntaxTree.getChildNodes())
            switch (child.getTokenString()) {
                case "IDENT" -> funCall.add("GOTO " + refs.get(child.getCharacter()));
                case "EXPRESSION_LIST" -> funCall.addAll(reverseList(expressionList(child)));
            }

        funCall.add("MOVE " + label);
        code.addAll(reverseList(funCall));
    }

    private List<String> expressionList(SyntaxTree syntaxTree) {
        List<String> expressionListCode = new ArrayList<>();

        for (SyntaxTree child : syntaxTree.getChildNodes())
            switch (syntaxTree.getTokenString()) {
                case "EXPRESSION" -> expressionListCode.addAll(genExpressionCode(child));
                case "EXPRESSION_LIST" -> expressionListCode.addAll(expressionList(child));
            }
        return expressionListCode;
    }

    private int posInRefs(String character) {
        if (refs.containsKey(character)) {
            int pos = 0;
            for (String key : refs.keySet()) {
                if (key.equals(character)) return pos;
                pos++;
            }
        }
        return -1;
    }

    private void function(SyntaxTree syntaxTree) {
        String functionName = "";

        String label = "L" + labelCounter++;
        code.add("LABEL " + label);

        List<SyntaxTree> parameterList = new ArrayList<>();
        for (SyntaxTree tree : syntaxTree.getChildNodes())
            switch (tree.getTokenString()) {
                case "IDENT" -> {
                    functionName = tree.getCharacter();
                    refs.put(functionName, label);
                }
                case "PARAMETER_LIST" -> {
                    parameterList = parameterList(tree);
                    for (SyntaxTree parameter : parameterList) {
                        code.add("STACKTOP " + posInRefs(parameter.getCharacter()) + ".idplace");
                    }
                }
                case "CLOSE_METH" -> {
                    for (int i = 0; i < parameterList.size(); i++)
                        code.add("POP");
                    for (SyntaxTree parameter : parameterList)
                        code.add("STORE " + posInRefs(parameter.getCharacter()) + ".idplace.value");
                    String labelNumber = label.substring(1);
                    code.add("POP R" + labelNumber);
                    code.add("LOAD " + posInRefs(functionName) + ".idplace.value");
                    code.add("MOVE R" + labelNumber);
                    code.add("GOTOSTACK");
                }
                case "STATEMENT" -> code.addAll(genExpressionCode(tree.getChildNodes().get(0)));
            }
    }

    private List<String> genExpressionCode(SyntaxTree syntaxTree) {
        List<String> expressionCode = new ArrayList<>();
        String lastIdent = "";
        for (SyntaxTree tree : expression(syntaxTree))
            switch (tree.getTokenString()) {
                case "IDENT" -> {
                    lastIdent = tree.getCharacter();
                    if (!refs.containsKey(tree.getCharacter()))
                        refs.put(tree.getCharacter(), null);
                    int ref = posInRefs(tree.getCharacter());
                    expressionCode.add("LOAD " + ref + ".idplace.value.value");
                }
                case "EQUAL" -> {
                    expressionCode.remove(expressionCode.size() - 1);
                    int ref = posInRefs(lastIdent);
                    expressionCode.add("STORE " + ref + ".idplace.value");
                }
                case "MULT" -> expressionCode.add("MUL");
                case "DIV" -> expressionCode.add("DIV");
                case "PLUS" -> expressionCode.add("ADD");
                case "MINUS" -> expressionCode.add("SUB");
                case "NUMBER" -> expressionCode.add("LOAD " + tree.getCharacter());
            }
        return expressionCode;
    }

    private List<SyntaxTree> parameterList(SyntaxTree syntaxTree) {
        List<SyntaxTree> list = new ArrayList<>();

        String tokenString = syntaxTree.getTokenString();
        if (tokenString.equals("LET") || tokenString.equals("KOMMA") || tokenString.equals("EPSILON")) return list;
        else if (tokenString.equals("IDENT")) {
            refs.put(syntaxTree.getCharacter(), null);
            list.add(syntaxTree);
        }

        List<SyntaxTree> child = syntaxTree.getChildNodes();
        for (SyntaxTree tree : child)
            list.addAll(parameterList(tree));

        return list;
    }

    private List<SyntaxTree> expression(SyntaxTree syntaxTree) {
        List<SyntaxTree> list = new ArrayList<>();

        List<SyntaxTree> child = syntaxTree.getChildNodes();
        for (SyntaxTree tree : child)
            list.addAll(term(tree));

        for (int i = 0; i < list.size() - 1; i++) {
            String item = list.get(i).getCharacter();
            if (item.equals("*") || item.equals("/") || item.equals("+") || item.equals("-"))
                Collections.swap(list, i, ++i);
        }

        List<String> charList = list.stream().map(SyntaxTree::getCharacter).toList();
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

    private List<SyntaxTree> term(SyntaxTree syntaxTree) {
        List<SyntaxTree> list = new ArrayList<>();

        String tokenStr = syntaxTree.getTokenString();
        if (tokenStr.equals("EPSILON") || tokenStr.equals("LET")) return list;
        else if (tokenStr.equals("FUNCTION_CALL")) {
            functionCall(syntaxTree);
            return list;
        } else if (!syntaxTree.getCharacter().isEmpty())
            list.add(syntaxTree);

        List<SyntaxTree> child = reverseList(syntaxTree.getChildNodes());
        for (SyntaxTree tree : child)
            list.addAll(term(tree));

        return list;
    }

    private <T> List<T> reverseList(List<T> list) {
        List<T> reverseList = new ArrayList<>(list);
        Collections.reverse(reverseList);
        return reverseList;
    }
}
