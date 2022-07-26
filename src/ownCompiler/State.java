package ownCompiler;

/**
 * @author Narwutsch Dominic created on 20.05.2022
 */
public enum State {
    NO_TYPE(-1, 0),
    EOF(-1, 255),
    START(0, 5, new String[]{}, new String[]{","},
            new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                    "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
                    "x", "y", "z"},
            new String[]{"("}, new String[]{")"}, new String[]{"+"},
            new String[]{"-"}, new String[]{"*"}, new String[]{"/"},
            new String[]{},
            new String[]{"fun"}, new String[]{"{"}, new String[]{"}"},
            new String[]{"="}, new String[]{";"}, new String[]{},
            new String[]{}, new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{"let", "Let", "LET"}, new String[]{"if", "IF", "If"},
            new String[]{"while", "While", "WHILE"},
            new String[]{"==", "<", ">", "<=", ">=", "!="},
            new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}),
    KOMMA(0, 7, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{";"}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{},
            new String[]{}),
    IDENT(0, 8,
            new String[]{}, new String[]{},
            new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
                    "i", "j", "k", "l", "m", "n", "o", "p", "q",
                    "r", "s", "t", "u", "v", "w", "x", "y",
                    "z"},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{},
            new String[]{}),
    OPEN_PAR(0, 9,
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{},
            new String[]{}),
    CLOSE_PAR(
            0, 10, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{}, new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    PLUS(0, 11,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    MINUS(0,
            12, new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    MUL(
            0, 13,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    DIV(
            0,
            14,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    NUM(
            0,
            1,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{
                    "."},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}),
    DIGIT(
            0,
            2,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{
                    "."},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}),
    FUN(
            1,
            35,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    OPEN_METH(
            0,
            23,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    CLOSE_METH(
            0,
            24,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    EQUAL(
            0,
            25,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    SEMICOLON(
            0,
            26,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    EXPRESSION_LIST(
            0,
            27,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    FUNCTION_CALL(
            0,
            28,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    EXPRESSION(
            0,
            15,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    TERM(
            0,
            17,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    RIGHT_TERM(
            0,
            18,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    OPERATOR(
            0,
            20,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    PROGRAM(
            0,
            21,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    RIGHT_EXPRESSION(
            0,
            16,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    LET(
            1,
            29,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    IF(
            1,
            30,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    WHILE(
            1,
            31,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{}),
    BOOL_OPERATOR(
            1,
            49,
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{},
            new String[]{});

    private final int prio;
    private final byte token;
    private String[] transitionStart;
    private String[] transitionKomma;
    private String[] transitionIdent;
    private String[] transitionOpenPar;
    private String[] transitionClosePar;
    private String[] transitionPlus;
    private String[] transitionMinus;
    private String[] transitionMul;
    private String[] transitionDiv;
    private String[] transitionNum;
    private String[] transitionFunction;
    private String[] transitionOpenMeth;
    private String[] transitionCloseMeth;
    private String[] transitionEqual;
    private String[] transitionSemicolon;
    private String[] transitionExpressionList;
    private String[] transitionFunctionCall;
    private String[] transitionExpression;
    private String[] transitionTerm;
    private String[] transitionRightTerm;
    private String[] transitionOperator;
    private String[] transitionProgram;
    private String[] transitionRightExpression;
    private String[] transitionLet;
    private String[] transitionIf;
    private String[] transitionWhile;
    private String[] transitionBoolOperator;
    private String[] transitionDigit;

    /**
     * @param prio
     * @param token
     * @param transitionStart
     * @param transitionKomma
     * @param transitionIdent
     * @param transitionOpenPar
     * @param transitionClosePar
     * @param transitionPlus
     * @param transitionMinus
     * @param transitionMul
     * @param transitionDiv
     * @param transitionNum
     * @param transitionFunction
     * @param transitionOpenMeth
     * @param transitionCloseMeth
     * @param transitionEqual
     * @param transitionSemicolon
     * @param transitionExpressionList
     * @param transitionFunctionCall
     * @param transitionExpression
     * @param transitionTerm
     * @param transitionRightTerm
     * @param transitionOperator
     * @param transitionProgram
     * @param transitionRightExpression
     * @param transitionLet
     * @param transitionIf
     * @param transitionWhile
     */
    State(int prio, Integer token, String[] transitionStart,
          String[] transitionKomma, String[] transitionIdent,
          String[] transitionOpenPar, String[] transitionClosePar,
          String[] transitionPlus, String[] transitionMinus,
          String[] transitionMul, String[] transitionDiv,
          String[] transitionNum, String[] transitionFunction,
          String[] transitionOpenMeth, String[] transitionCloseMeth,
          String[] transitionEqual, String[] transitionSemicolon,
          String[] transitionExpressionList, String[] transitionFunctionCall,
          String[] transitionExpression, String[] transitionTerm,
          String[] transitionRightTerm, String[] transitionOperator,
          String[] transitionProgram, String[] transitionRightExpression,
          String[] transitionLet, String[] transitionIf,
          String[] transitionWhile, String[] transitionBoolOperator, String[] transitionDigit) {
        this.prio = prio;
        this.token = token.byteValue();
        this.transitionStart = transitionStart;
        this.transitionKomma = transitionKomma;
        this.transitionIdent = transitionIdent;
        this.transitionOpenPar = transitionOpenPar;
        this.transitionClosePar = transitionClosePar;
        this.transitionPlus = transitionPlus;
        this.transitionMinus = transitionMinus;
        this.transitionMul = transitionMul;
        this.transitionDiv = transitionDiv;
        this.transitionNum = transitionNum;
        this.transitionFunction = transitionFunction;
        this.transitionOpenMeth = transitionOpenMeth;
        this.transitionCloseMeth = transitionCloseMeth;
        this.transitionEqual = transitionEqual;
        this.transitionSemicolon = transitionSemicolon;
        this.transitionExpressionList = transitionExpressionList;
        this.transitionFunctionCall = transitionFunctionCall;
        this.transitionExpression = transitionExpression;
        this.transitionTerm = transitionTerm;
        this.transitionRightTerm = transitionRightTerm;
        this.transitionOperator = transitionOperator;
        this.transitionProgram = transitionProgram;
        this.transitionRightExpression = transitionRightExpression;
        this.transitionLet = transitionLet;
        this.transitionIf = transitionIf;
        this.transitionWhile = transitionWhile;
        this.transitionBoolOperator = transitionBoolOperator;
        this.transitionDigit = transitionDigit;
    }

    State(int prio, Integer token) {
        this.prio = prio;
        this.token = token.byteValue();

    }

    public int getPrio() {
        return prio;
    }

    public byte getToken() {
        return token;
    }

    public String[] getTransitionSet(State nextState) {
        switch (nextState) {
            default:
            case NO_TYPE:
            case EOF:
                return new String[0];
            case START:
                return transitionStart;
            case KOMMA:
                return transitionKomma;
            case IDENT:
                return transitionIdent;
            case OPEN_PAR:
                return transitionOpenPar;
            case CLOSE_PAR:
                return transitionClosePar;
            case PLUS:
                return transitionPlus;
            case MINUS:
                return transitionMinus;
            case MUL:
                return transitionMul;
            case DIV:
                return transitionDiv;
            case NUM:
                return transitionNum;
            case FUN:
                return transitionFunction;
            case OPEN_METH:
                return transitionOpenMeth;
            case CLOSE_METH:
                return transitionCloseMeth;
            case EQUAL:
                return transitionEqual;
            case SEMICOLON:
                return transitionSemicolon;
            case EXPRESSION_LIST:
                return transitionExpressionList;
            case FUNCTION_CALL:
                return transitionFunctionCall;
            case EXPRESSION:
                return transitionExpression;
            case TERM:
                return transitionTerm;
            case RIGHT_TERM:
                return transitionRightTerm;
            case OPERATOR:
                return transitionOperator;
            case PROGRAM:
                return transitionProgram;
            case RIGHT_EXPRESSION:
                return transitionRightExpression;
            case IF:
                return transitionIf;
            case WHILE:
                return transitionWhile;
            case LET:
                return transitionLet;
            case BOOL_OPERATOR:
                return transitionBoolOperator;
            case DIGIT:
                return transitionDigit;

        }
    }
}
