package ownCompiler;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author Narwutsch Dominic created on 16.07.2022
 */
public class SDKCodeGen implements TokenList {
    private final List<String> code = new ArrayList<>();
    private final Map<String, String> refs = new LinkedHashMap<>();
    private final Map<String, String> functionRefs = new HashMap<>();
    private final Map<String, List<String>> params = new HashMap<>();
    private int labelCounter = 1;

    List<String> generatePseudoCode(SyntaxTree syntaxTree) {
        HashMap<String, String> scope = new HashMap<>();
        generateCode(syntaxTree, scope);
        code.add("HALT");
        return code;
    }

    private void generateCode(SyntaxTree syntaxTree, HashMap<String, String> scope) {
        if (syntaxTree.getToken() == EPSILON)
            return;

        List<SyntaxTree> child = new ArrayList<>();
        switch (syntaxTree.getToken()) {
            case FUNCTION -> function(syntaxTree, new HashMap<>(scope));
            case EXPRESSION -> code.addAll(genExpressionCode(syntaxTree, scope, null));
            case WHILE_STATEMENT -> code.addAll(whileStatement(syntaxTree, new HashMap<>(scope)));
            default -> child = syntaxTree.getChildNodes();
        }

        for (SyntaxTree tree : child)
            generateCode(tree, scope);
    }

    private LinkedList<String> whileStatement(SyntaxTree syntaxTree, HashMap<String, String> scope) {
        System.out.println(syntaxTree.getTokenString());
        LinkedList<String> whileStmnt = new LinkedList<>();

        String label = "L" + labelCounter++;
        whileStmnt.add("LABEL " + label);

        String outLabel = "L" + labelCounter++;

        for (SyntaxTree child : syntaxTree.getChildNodes()) {
            switch (child.getToken()) {
                case EXPRESSION -> {
                    whileStmnt.addAll(boolStatement(syntaxTree, scope));
                    whileStmnt.add("GOFALSE " + outLabel);
                }
                case STATEMENT -> {
                    if (!child.getChildNodes().isEmpty()) {
                        whileStmnt.addAll(genExpressionCode(child.getChildNodes().get(0), scope, null));
                    }
                }
            }
        }

        whileStmnt.add("GOTO " + label);
        whileStmnt.add("LABEL " + outLabel);

        return whileStmnt;
    }

    private Collection<String> boolStatement(SyntaxTree syntaxTree, HashMap<String, String> scope) {
        List<String> boolStmnt = new LinkedList<>();
        List<SyntaxTree> childs = syntaxTree.getChildNodes();
        if (syntaxTree.getToken() == BOOL_STATEMENT) {
            String ident = null;
            String operator = null;
            for (SyntaxTree child : childs)
                switch (child.getToken()) {
                    case TERM -> boolStmnt.addAll(genExpressionCode(syntaxTree.getChild(2), scope, null));
                    case IDENT -> ident = "LOAD " + posInRefs(scope.get(child.getCharacter())) + ".idplace.value.value";
                    case BOOL_OPERATOR -> operator = switch (child.getCharacter()) {
                        case "==" -> "EQUAL";
                        case "<" -> "LESS";
                        case ">" -> "GREATER";
                        case "<=" -> "LESS_EQUAL";
                        case ">=" -> "GREATER_EQUAL";
                        case "!=" -> "NOT_EQUAL";
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

    private void functionCall(SyntaxTree syntaxTree, HashMap<String, String> scope) {
        LinkedList<String> funCall = new LinkedList<>();
        String functionName = "";

        String label = "L" + labelCounter++;
        funCall.add("LABEL " + label);

        functionName = createPostfixAnnotitation(syntaxTree, scope, funCall,
                functionName);

        for (String param : reverseList(params.get(functionName))) {
            funCall.add(posInRefs(param) + ".idplace.value");
        }

        funCall.add("MOVE " + label);
        code.addAll(reverseList(funCall));
    }

    private String createPostfixAnnotitation(
            SyntaxTree syntaxTree,
            HashMap<String, String> scope,
            LinkedList<String> funCall,
            String functionName
    ) {
        for (SyntaxTree child : syntaxTree.getChildNodes())
            switch (child.getToken()) {
                case IDENT -> {
                    functionName = child.getCharacter();
                    String fun = functionRefs.get(child.getCharacter());
                    funCall.add("GOTO " + refs.get(fun));
                }
                case EXPRESSION_LIST -> funCall
                        .addAll(reverseList(expressionList(child, scope)));
            }
        return functionName;
    }

    private List<String> expressionList(SyntaxTree syntaxTree, HashMap<String, String> scope) {
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
                if (key.equals(character))
                    return pos;
                pos++;
            }
        }
        return -1;
    }

    private void function(SyntaxTree syntaxTree, HashMap<String, String> scope) {
        String functionName = "";
        AtomicReference<String> lastEquation = new AtomicReference<>(null);

        String label = "L" + labelCounter++;
        code.add("LABEL " + label);

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
                    for (String parameter : parameterList.values()) {
                        code.add("STACKTOP "
                                + posInRefs(parameter)
                                + ".idplace");
                        params.get(functionName).add(parameter);
                    }
                }
                case CLOSE_METH -> {
                    for (int i = 0; i < parameterList.size(); i++)
                        code.add("POP");
                    for (String parameter : parameterList.values()) {
                        code.add("STORE " + posInRefs(parameter)
                                + ".idplace.value");
                    }
                    String labelNumber = label.substring(1);
                    code.add("POP R" + labelNumber);
                    code.add("LOAD " + posInRefs(lastEquation.get())
                            + ".idplace.value");
                    code.add("MOVE R" + labelNumber);
                    code.add("GOTOSTACK");
                }
                case STATEMENT -> {
                    if (!tree.getChildNodes().isEmpty()) code
                            .addAll(genExpressionCode(tree.getChildNodes().get(0), scope, lastEquation::set));
                }
                case WHILE_STATEMENT -> code.addAll(whileStatement(tree, new HashMap<>(scope)));
            }
    }

    private List<String> genExpressionCode(SyntaxTree syntaxTree, HashMap<String, String> scope, Consumer<String> o) {
        List<String> expressionCode = new ArrayList<>();
        String lastIdent = "";
        for (SyntaxTree tree : expression(syntaxTree, scope))
            switch (tree.getToken()) {
                case IDENT -> {
                    lastIdent = tree.getCharacter();
                    String par = scope.get(tree.getCharacter());
                    int ref = posInRefs(par);
                    expressionCode.add("LOAD " + ref + ".idplace.value.value");
                }
                case EQUAL -> {
                    expressionCode.remove(expressionCode.size() - 1);
                    String par = scope.get(lastIdent);
                    int ref = posInRefs(par);
                    expressionCode.add("STORE " + ref + ".idplace.value");
                    if (o != null) o.accept(par);
                }
                case MULT -> expressionCode.add("MUL");
                case DIV -> expressionCode.add("DIV");
                case PLUS -> expressionCode.add("ADD");
                case MINUS -> expressionCode.add("SUB");
                case DIGIT -> expressionCode.add("LOAD " + tree.getCharacter());
            }
        return expressionCode;
    }

    private Map<String, String> parameterList(SyntaxTree syntaxTree, HashMap<String, String> scope) {
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

    private List<SyntaxTree> expression(SyntaxTree syntaxTree, HashMap<String, String> scope) {
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

    private List<SyntaxTree> term(SyntaxTree syntaxTree, HashMap<String, String> scope) {
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

    private List<SyntaxTree> createSymbol(SyntaxTree tree, HashMap<String, String> scope) {
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

    private <T> List<T> reverseList(List<T> list) {
        List<T> reverseList = new ArrayList<>(list);
        Collections.reverse(reverseList);
        return reverseList;
    }
}
