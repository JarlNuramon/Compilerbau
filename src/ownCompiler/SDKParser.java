package ownCompiler;

public class SDKParser extends SDKScanner {

    // Konstante für Ende der Eingabe
    public final char EOF = (char) 255;
    // Zeiger auf das aktuelle Eingabezeichen
    private int pointer;
    // Zeiger auf das Ende der Eingabe
    private int maxPointer;
    private SyntaxTree parseTree;

    // -------------------------------------------------------------------------
    // ------------Konstruktor der Klasse ArithmetikParserClass-----------------
    // -------------------------------------------------------------------------

    SDKParser(SyntaxTree parseTree) {
        this.parseTree = parseTree;
        // this.input = new char[256];
        this.pointer = 0;
        this.maxPointer = 0;
    }

    // -------------------------------------------------------------------------
    // -------------------Methoden der Grammatik--------------------------------
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // program -> function program | function
    // Der Parameter sT ist die Wurzel des bis hier geparsten Syntaxbaumes
    // -------------------------------------------------------------------------
    boolean program(SyntaxTree sT) {
        byte[] funSet = {FUNCTION};
        if (match(funSet, sT))
            // program -> function program
            return function(sT) && program(sT.insertSubtree(PROGRAM));
        else
            return expression(sT.insertSubtree(EXPRESSION));

    }// program

    private boolean function(SyntaxTree subtree) {
        byte[] openParSet = {OPEN_PAR};
        byte[] closeParSet = {CLOSE_PAR};

        boolean fun = ident(subtree.insertSubtree(IDENT));
        if (match(openParSet, subtree)) {
            fun = fun && parameterlist(subtree.insertSubtree(PARAMETER_LIST));
            if (match(closeParSet, subtree))
                return fun && funOpen(subtree) && expression(subtree.insertSubtree(EXPRESSION)) && funClose(subtree);
            else {// Syntaxfehler
                syntaxError("Geschlossene Parameter Klammer erwartet");
                return false;
            }
        } else {// Syntaxfehler
            syntaxError("Öffnende Parameter Klammer erwartet");
            return false;
        }
    }

    private boolean funClose(SyntaxTree subtree) {
        byte[] closeMethSet = {CLOSE_METH};

        if (match(closeMethSet, subtree))
            return true;
        else {// Syntaxfehler
            syntaxError("Geschlossene Methoden Klammer erwartet");
            return false;
        }
    }

    private boolean fun(SyntaxTree subtree) {
        byte[] funSet = {FUNCTION};

        if (match(funSet, subtree))
            return true;
        else {// Syntaxfehler
            syntaxError("fun declaration erwartet");
            return false;
        }
    }

    private boolean funOpen(SyntaxTree subtree) {
        byte[] closeMethSet = {OPEN_METH};

        if (match(closeMethSet, subtree))
            return true;
        else {// Syntaxfehler
            syntaxError("Öffnende Methoden Klammer erwartet");
            return false;
        }
    }

    private boolean parameterlist(SyntaxTree subtree) {
        byte[] komaSet = {KOMMA};
        byte[] letSet = {LET};
        byte[] identSet = {IDENT};

        if (match(letSet, subtree)) {
            if (lookAhead(komaSet))
                if (match(identSet, subtree)) {
                    match(komaSet, subtree);
                    return parameterlist(subtree.insertSubtree(PARAMETER_LIST));
                } else {
                    syntaxError("Identifier erwartet");
                    return false;
                }
            else {
                if (match(identSet, subtree))
                    return true;
                else {
                    syntaxError("Identifier erwartet");
                    return false;
                }
            }
        } else {// Epsilon Tree
            subtree.insertSubtree(EPSILON);
            return true;
        }

    }

    // Not Sure about how to check Epsilon tree
    private boolean expressionList(SyntaxTree subtree) {
        byte[] kommaSet = {KOMMA};

        if (expression(subtree.insertSubtree(EXPRESSION))) {
            if (match(kommaSet, subtree))
                return expressionList(subtree.insertSubtree(TokenList.EXPRESSION_LIST));
            else
                return true;
        } else {// Epsilon Tree
            subtree.insertSubtree(EPSILON);
            return true;
        }
    }

    private boolean ident(SyntaxTree subtree) {
        byte[] closeMethSet = {IDENT};

        if (match(closeMethSet, subtree))
            return true;
        else {// Syntaxfehler
            syntaxError("method identifier erwartet");
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // expression -> term rightExpression
    // Der Parameter sT ist die Wurzel des bis hier geparsten Syntaxbaumes
    // -------------------------------------------------------------------------
    boolean expression(SyntaxTree sT) {
        return (term(sT.insertSubtree(TERM)) && rightExpression(sT.insertSubtree(RIGHT_EXPRESSION)));
    }// expression

    // -------------------------------------------------------------------------
    // rightExpression -> '+' term rightExpression |
    // '-' term rightExpression | Epsilon
    // Der Parameter sT ist die Wurzel des bis hier geparsten Syntaxbaumes
    // -------------------------------------------------------------------------
    boolean rightExpression(SyntaxTree sT) {
        byte[] addSet = {PLUS};
        byte[] subSet = {MINUS};
        SyntaxTree epsilonTree;
        // Falls aktuelles Token PLUS
        if (match(addSet, sT))
            // rightExpression -> '+' term rightExpression
            return term(sT.insertSubtree(TERM)) && rightExpression(sT.insertSubtree(RIGHT_EXPRESSION));
            // Falls aktuelles Token MINUS
        else if (match(subSet, sT))
            // rightExpression -> '-' term rightExpression
            return term(sT.insertSubtree(TERM)) && rightExpression(sT.insertSubtree(RIGHT_EXPRESSION));
            // sonst
        else {
            // rightExpression ->Epsilon
            epsilonTree = sT.insertSubtree(EPSILON);
            return true;
        }
    }// rightExpression

    // -------------------------------------------------------------------------
    // term -> operator rightTerm
    // Der Parameter sT ist die Wurzel des bis hier geparsten Syntaxbaumes
    // -------------------------------------------------------------------------

    boolean term(SyntaxTree sT) {
        byte[] identSet = {IDENT};
        byte[] equalSet = {EQUAL};

        if (lookAhead(identSet)) {
            boolean defOperator = defOperator(sT.insertSubtree(DEF_OPERATOR));
            if (match(equalSet, sT))
                return defOperator && operator(sT.insertSubtree(OPERATOR)) && rightTerm(sT.insertSubtree(TokenList.RIGHT_TERM));
            else {
                syntaxError("Gleichheitszeichen erwartet");
                return false;
            }
        } else
            // term -> operator rightTerm
            return (operator(sT.insertSubtree(OPERATOR)) && rightTerm(sT.insertSubtree(RIGHT_TERM)));
    }// term

    private boolean defOperator(SyntaxTree subtree) {
        byte[] letSet = {LET};

        if (match(letSet, subtree))
            return ident(subtree);
        else {// Syntaxfehler
            syntaxError("let erwartet");
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // rightTerm -> '*' operator rightTerm |
    // '/' operator rightTerm | Epsilon
    // Der Parameter sT ist die Wurzel des bis hier geparsten Syntaxbaumes
    // -------------------------------------------------------------------------

    boolean rightTerm(SyntaxTree sT) {
        byte[] multDivSet = {MULT, DIV};
        byte[] divSet = {DIV};
        SyntaxTree epsilonTree;

        // Falls aktuelles Token MULT oder DIV
        if (match(multDivSet, sT))
            // rightTerm -> '*' operator rightTerm bzw.
            // rightTerm -> '/' operator rightTerm
            return operator(sT.insertSubtree(OPERATOR)) && rightTerm(sT.insertSubtree(RIGHT_TERM));
        else {
            // rightTerm ->Epsilon
            epsilonTree = sT.insertSubtree(EPSILON);
            return true;
        }
    }// rightTerm

    // -------------------------------------------------------------------------
    // operator -> '(' expression ')' | num
    // Der Parameter sT ist die Wurzel des bis hier geparsten Syntaxbaumes
    // -------------------------------------------------------------------------
    boolean operator(SyntaxTree sT) {
        byte[] openParSet = {OPEN_PAR};
        byte[] closeParSet = {CLOSE_PAR};
        byte[] numSet = {NUM};
        byte[] identSet = {IDENT};

        // Falls aktuelle Eingabe '('
        if (match(openParSet, sT))
            // operator -> '(' expression ')'
            if (expression(sT.insertSubtree(EXPRESSION))) {
                // Fallunterscheidung ermöglicht, den wichtigen Fehler einer
                // fehlenden geschlossenen Klammer gesondert auszugeben
                if (match(closeParSet, sT))
                    return true;
                else {// Syntaxfehler
                    syntaxError("Geschlossene Klammer erwartet");
                    return false;
                }
            } else {
                syntaxError("Fehler in geschachtelter Expression");
                return false;
            }
            // sonst versuchen nach num abzuleiten
        else if (match(numSet, sT))
            // operator -> num
            return true;
        else if (match(identSet, sT))
            // operator -> ident
            return true;
            // operator -> function call
        else if (functionCall(sT.insertSubtree(FUNCTION_CALL)))
            return true;
            // wenn das nicht möglich ...
        else {
            syntaxError("Ziffer, Identifier, Klammer auf oder Function Call erwartet");
            return false;
        }
    }// operator

    private boolean functionCall(SyntaxTree subTree) {
        byte[] identSet = {IDENT};
        byte[] openParSet = {OPEN_PAR};
        byte[] closeParSet = {CLOSE_PAR};

        if (match(identSet, subTree))
            if (match(openParSet, subTree))
                if (expressionList(subTree.insertSubtree(EXPRESSION))) {
                    // Fallunterscheidung ermöglicht, den wichtigen Fehler einer
                    // fehlenden geschlossenen Klammer gesondert auszugeben
                    if (match(closeParSet, subTree))
                        return true;
                    else {// Syntaxfehler
                        syntaxError("Geschlossene Klammer erwartet");
                        return false;
                    }
                } else {
                    syntaxError("Fehler in geschachtelter Expression List");
                    return false;
                }
            else {// Syntaxfehler
                syntaxError("Geöffnete Klammer erwartet");
                return false;
            }
        else {// Syntaxfehler
            syntaxError("Identifier erwartet");
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // -------------------Hilfsmethoden-----------------------------------------
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Methode, die testet, ob das aktuele Token unter den Token
    // ist, die als Parameter (matchSet) übergeben wurden.
    // Ist das der Fall, so gibt match() true zurück und setzt den Eingabe-
    // zeiger auf das nächste Zeichen, sonst wird false zurückgegeben.
    // -------------------------------------------------------------------------
    boolean match(byte[] matchSet, SyntaxTree sT) {
        SyntaxTree node;
        for (byte b : matchSet)
            if (tokenStream.get(pointer).token.getToken() == b) {
                // gefundenes Token in den Syntaxbaum eintragen
                sT.insertSubtree(tokenStream.get(pointer).token.getToken());
                pointer++; // Eingabepointer auf das nächste Zeichen setzen
                return true;
            }
        return false;
    }// match

    // -------------------------------------------------------------------------
    // Methode, die testet, ob das auf das aktuelle Token folgende Token
    // unter den Token ist, die als Parameter (aheadSet) übergeben wurden.
    // Der Eingabepointer wird nicht verändert!
    // -------------------------------------------------------------------------
    boolean lookAhead(byte[] aheadSet) {
        if (pointer >= tokenStream.size() - 1)
            return false;
        for (byte b : aheadSet)
            if (tokenStream.get(pointer + 1).token.getToken() == b)
                return true;
        return false;
    }// lookAhead

    // -------------------------------------------------------------------------
    // Methode, die testet, ob das Ende der Eingabe erreicht ist
    // (pointer == maxPointer)
    // -------------------------------------------------------------------------
    boolean inputEmpty() {
        if (pointer == (tokenStream.size() - 1)) {
            ausgabe("Eingabe leer!", 0);
            return true;
        } else {
            syntaxError("Eingabe bei Ende des Parserdurchlaufs nicht leer");
            return false;
        }

    }// inputEmpty

    // -------------------------------------------------------------------------
    // Methode zum korrekt eingerückten Ausgeben des Syntaxbaumes auf der
    // Konsole
    // -------------------------------------------------------------------------
    void ausgabe(String s, int t) {
        for (int i = 0; i < t; i++)
            System.out.print("  ");
        System.out.println(s);
    }// ausgabe

    // -------------------------------------------------------------------------
    // Methode zum Ausgeben eines Syntaxfehlers mit Angabe des vermuteten
    // Zeichens, bei dem der Fehler gefunden wurde
    // -------------------------------------------------------------------------
    void syntaxError(String s) {
        char z;
        if (tokenStream.get(pointer).token.getToken() == EOF)
            System.out.println("Syntax Fehler in Zeile " + tokenStream.get(pointer).line + ": " + "EOF");
        else
            System.out.println(
                    "Syntax Fehler in Zeile " + tokenStream.get(pointer).line + ": " + tokenStream.get(pointer).token);
        System.out.println(s);
    }// syntaxError

}
