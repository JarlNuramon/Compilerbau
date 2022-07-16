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
    void updatePointer() {
        maxPointer = tokenStream.size() - 1;
        System.out.println("Stream Size: " + maxPointer);
    }

    boolean program(SyntaxTree sT) {
        byte[] funSet = {FUNCTION};
        boolean value = true;
        if (match(true, sT, funSet))
            // program -> function program
            value = value && function(sT);
        else
            value = value && statement(sT);
        if (pointer < maxPointer && value)
            value = value && program(sT);
        return value;

    }// program

    boolean statement(SyntaxTree sT) {
        if (lookCurrent(CLOSE_METH) || lookCurrent(FUNCTION) || lookCurrent(((byte) EOF))) {
            return true;
        }
        SyntaxTree sub = sT.insertSubtree(STATEMENT);
        if (expression(sub.insertSubtree(EXPRESSION)) && semicolon(sub))
            return statement(sT);
        return false;

    }

    private boolean function(SyntaxTree subtree) {
        byte[] openParSet = {OPEN_PAR};
        byte[] closeParSet = {CLOSE_PAR};

        boolean fun = ident(subtree);
        if (match(true, subtree, openParSet)) {
            fun = fun && parameterlist(subtree.insertSubtree(PARAMETER_LIST));
            if (match(true, subtree, closeParSet))
                return fun && funOpen(subtree) && statement(subtree) && funClose(subtree);
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

        if (match(true, subtree, closeMethSet))
            return true;
        else {// Syntaxfehler
            syntaxError("Geschlossene Methoden Klammer erwartet");
            return false;
        }
    }

    private boolean fun(SyntaxTree subtree) {
        byte[] funSet = {FUNCTION};

        if (match(true, subtree, funSet))
            return true;
        else {// Syntaxfehler
            syntaxError("fun declaration erwartet");
            return false;
        }
    }

    private boolean funOpen(SyntaxTree subtree) {
        byte[] closeMethSet = {OPEN_METH};

        if (match(true, subtree, closeMethSet))
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

        if (match(true, subtree, letSet)) {
            if (lookAhead(komaSet))
                if (match(true, subtree, identSet)) {
                    match(true, subtree, komaSet);
                    return parameterlist(subtree.insertSubtree(PARAMETER_LIST));
                } else {
                    syntaxError("Identifier erwartet");
                    return false;
                }
            else {
                if (match(true, subtree, identSet))
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
        if (lookAhead(kommaSet))
            return expression(subtree.insertSubtree(EXPRESSION)) && match(true, subtree, kommaSet)
                    && expressionList(subtree.insertSubtree(EXPRESSION_LIST));
        else if (expression(subtree.insertSubtree(EXPRESSION)))
            return true;
        else {// Epsilon Tree
            subtree.insertSubtree(EPSILON);
            return true;
        }
    }

    private boolean ident(SyntaxTree subtree) {
        byte[] identSet = {IDENT};

        if (match(true, subtree, identSet))
            return true;
        else {// Syntaxfehler
            syntaxError("method identifier erwartet");
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // expression -> term rightExpression ;
    // Der Parameter sT ist die Wurzel des bis hier geparsten Syntaxbaumes
    // -------------------------------------------------------------------------
    boolean expression(SyntaxTree sT) {
        return (term(sT.insertSubtree(TERM)) && rightExpression(sT.insertSubtree(RIGHT_EXPRESSION)));
    }// expression

    private boolean semicolon(SyntaxTree sT) {
        if (match(true, sT, SEMICOLON)) {
            return true;
        }
        syntaxError("Semicolon erwartet");
        return false;
    }

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
        if (match(true, sT, addSet))
            // rightExpression -> '+' term rightExpression
            return term(sT.insertSubtree(TERM)) && rightExpression(sT.insertSubtree(RIGHT_EXPRESSION));
            // Falls aktuelles Token MINUS
        else if (match(true, sT, subSet))
            // rightExpression -> '-' term rightExpression
            return term(sT.insertSubtree(TERM)) && rightExpression(sT.insertSubtree(RIGHT_EXPRESSION));
            // sonst
        else {
            // rightExpression ->Epsilon
            sT.insertSubtree(EPSILON);
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
            if (match(true, sT, equalSet))
                return defOperator && operator(sT.insertSubtree(OPERATOR))
                        && rightTerm(sT.insertSubtree(TokenList.RIGHT_TERM));
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

        if (match(true, subtree, letSet))
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
        ;

        // Falls aktuelles Token MULT oder DIV
        if (match(true, sT, multDivSet))
            // rightTerm -> '*' operator rightTerm bzw.
            // rightTerm -> '/' operator rightTerm
            return operator(sT.insertSubtree(OPERATOR)) && rightTerm(sT.insertSubtree(RIGHT_TERM));
        else {
            // rightTerm ->Epsilon
            sT.insertSubtree(EPSILON);
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
        if (match(true, sT, openParSet))
            // operator -> '(' expression ')'
            if (expression(sT.insertSubtree(EXPRESSION))) {
                // Fallunterscheidung ermöglicht, den wichtigen Fehler einer
                // fehlenden geschlossenen Klammer gesondert auszugeben
                if (match(true, sT, closeParSet))
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
        else if (match(true, sT, numSet))
            // operator -> num
            return true;
        else if (lookAhead(openParSet) && functionCall(sT.insertSubtree(FUNCTION_CALL)))
            return true;
        else if (match(true, sT, identSet))
            // operator -> ident
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

        if (match(true, subTree, identSet))
            if (match(true, subTree, openParSet))
                if (expressionList(subTree.insertSubtree(EXPRESSION_LIST))) {
                    // Fallunterscheidung ermöglicht, den wichtigen Fehler einer
                    // fehlenden geschlossenen Klammer gesondert auszugeben
                    if (match(true, subTree, closeParSet))
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
    boolean match(SyntaxTree sT, byte... matchSet) {
        return match(false, sT, matchSet);
    }// match

    boolean match(boolean addLexem, SyntaxTree sT, byte... matchSet) {
        SyntaxTree node;
        for (byte b : matchSet)
            if (tokenStream.get(pointer).token.getToken() == b) {
                // gefundenes Token in den Syntaxbaum eintragen
                SyntaxTree sub = sT.insertSubtree(tokenStream.get(pointer).token.getToken());
                if (addLexem)
                    sub.setCharacter(tokenStream.get(pointer).lexem);
                pointer++; // Eingabepointer auf das nächste Zeichen setzen
                return true;
            }
        return false;
    }

    boolean lookCurrent(byte... aheadSet) {

        for (byte b : aheadSet)
            if (tokenStream.get(pointer).token.getToken() == b)
                return true;
        return false;
    }

    boolean lookAhead(byte... aheadSet) {
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
