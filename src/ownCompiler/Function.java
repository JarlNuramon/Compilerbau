package ownCompiler;

public enum Function {

	PROGRAMM(""),
	FUNCTION(""),
	PARAMETER_LIST(""),
	EXPRESSION(""),
	TERM(""),
	PLUS(""),
	RIGHT_EXPRESSION(""),
	OPERATOR(""),
	FUNCTION_CALL(""),
	EXPRESSION_LIST("");
	
	public final String regex;
	
	private Function(String regex) {
        this.regex = regex;
    }
}
