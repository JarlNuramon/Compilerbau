package sdk.stack;

import static sdk.model.PseudoCodeConstants.ADD_PSEUD;
import static sdk.model.PseudoCodeConstants.DIV_PSEUD;
import static sdk.model.PseudoCodeConstants.EQUAL_PSEUD;
import static sdk.model.PseudoCodeConstants.GOFALSE;
import static sdk.model.PseudoCodeConstants.GOTO;
import static sdk.model.PseudoCodeConstants.GOTOSTACK;
import static sdk.model.PseudoCodeConstants.GREATER_EQUAL_PSEUD;
import static sdk.model.PseudoCodeConstants.GREATER_PSEUD;
import static sdk.model.PseudoCodeConstants.HALT;
import static sdk.model.PseudoCodeConstants.LABEL;
import static sdk.model.PseudoCodeConstants.LESS_EQUAL_PSEUD;
import static sdk.model.PseudoCodeConstants.LESS_PSEUD;
import static sdk.model.PseudoCodeConstants.LOAD;
import static sdk.model.PseudoCodeConstants.MOVE;
import static sdk.model.PseudoCodeConstants.MUL_PSEUD;
import static sdk.model.PseudoCodeConstants.NOT_EQUAL_PSEUD;
import static sdk.model.PseudoCodeConstants.OUT;
import static sdk.model.PseudoCodeConstants.POP;
import static sdk.model.PseudoCodeConstants.STACKTOP;
import static sdk.model.PseudoCodeConstants.STORE;
import static sdk.model.PseudoCodeConstants.SUB_PSEUD;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class KellerMaschineSDK {

	private final boolean isDebug;

	private Map<String, Symbol> symbolTabelle = new HashMap<>();
	private Deque<String> stack = new ArrayDeque<>();
	private Map<String, String> register = new HashMap<>();
	private Map<String, Integer> codeLineByLabel = new HashMap<>();
	private String lastLabel;

	public KellerMaschineSDK(boolean isDebug) {
		this.isDebug = isDebug;
	}
	public void runPseudoCode(List<String> code) {
		log("");
		for (int i = 0; i < code.size(); i++) {
			String codeLine = code.get(i);
			if (codeLine.contains(LABEL))
				codeLineByLabel.put(codeLine.substring(5).trim(), i);
		}
		for (Entry<String, Integer> entry : codeLineByLabel.entrySet()) {
			log("Label %s at %s", entry.getKey(), entry.getValue());
		}
		log("Start programm");
		log("");
		int i = 0;
		while (true) {
			String codeLine = code.get(i);
			String[] splitString = codeLine.split(" ");
			switch (splitString[0]) {
				case LABEL :
					labelMethod(splitString);
					break;
				case LOAD :
					loadMethod(splitString);
					break;
				case STORE :
					storeMethod(splitString);
					break;
				case STACKTOP :
					stacktopMethod(splitString);
					break;
				case POP :
					String s = pop();
					if (splitString.length > 1) {
						log("Added %s to list as %s", s, splitString[1]);
						register.put(splitString[1], s);
					}
					break;
				case HALT :
					log("End of Program");
					return;
				case OUT :
					System.out.println("OUTPUT: " + stack.pop());
					break;
				case SUB_PSEUD :
					minusMethod();
					break;
				case ADD_PSEUD :
					plusMethod();
					break;
				case DIV_PSEUD :
					divMethod();
					break;
				case MUL_PSEUD :
					mulMethod();
					break;
				case GOTOSTACK :
					String labelFromStack = pop();
					i = codeLineByLabel.get(labelFromStack) - 1;
					log("GOTO %s at line %s", labelFromStack, i + 1);
					break;
				case NOT_EQUAL_PSEUD :
					notEqualMethod();
					break;
				case GREATER_EQUAL_PSEUD :
					greaterEqualMethod();
					break;
				case LESS_EQUAL_PSEUD :
					lessEqualMethod();
					break;
				case GREATER_PSEUD :
					greaterMethod();
					break;
				case LESS_PSEUD :
					lessMethod();
					break;
				case EQUAL_PSEUD :
					equalMethod();
					break;
				case GOTO :
					i = codeLineByLabel.get(splitString[1]) - 1;
					log("GOTO %s at line %s", splitString[1], i + 1);
					break;

				case GOFALSE :
					String goFalse = pop();
					if (!goFalse.equals("1")) {
						i = codeLineByLabel.get(splitString[1]) - 1;
						log("GOTO %s at line %s", splitString[1], i + 1);
					}
					break;
				case "" :
					break;
				case MOVE :
					String label;
					if (splitString[1].contains("R")) {
						label = register.getOrDefault(splitString[1], null);
					} else
						label = splitString[1];
					log("Move found %s", label);
					if (label != null) {
						push(label);
						register.remove(splitString[1]);
					}
					break;
				default :
					throw new RuntimeException("UNKNOWN PSEUDOCODE");
			}
			i++;
		}
	}

	private void equalMethod() {
		String value1 = pop();
		String value2 = pop();
		if (value1.equals(value2))
			push(1 + "");
		else
			push(0 + "");

	}
	private void notEqualMethod() {
		String value1 = pop();
		String value2 = pop();
		if (!value1.equals(value2))
			push(1 + "");
		else
			push(0 + "");

	}
	private void greaterMethod() {
		Float value1 = Float.parseFloat(pop());
		Float value2 = Float.parseFloat(pop());
		if (value1 > value2)
			push(1 + "");
		else
			push(0 + "");
	}
	private void lessMethod() {
		Float value1 = Float.parseFloat(pop());
		Float value2 = Float.parseFloat(pop());
		if (value1 < value2)
			push(1 + "");
		else
			push(0 + "");
	}
	private void greaterEqualMethod() {
		Float value1 = Float.parseFloat(pop());
		Float value2 = Float.parseFloat(pop());
		if (value1 >= value2)
			push(1 + "");
		else
			push(0 + "");
	}
	private void lessEqualMethod() {
		Float value1 = Float.parseFloat(pop());
		Float value2 = Float.parseFloat(pop());
		if (value1 <= value2)
			push(1 + "");
		else
			push(0 + "");
	}
	private void log(String s, Object... objects) {
		if (isDebug) {
			System.out.println(String.format(s, objects));
		}
	}
	private void mulMethod() {
		String firstArg = pop();
		String secondArg = pop();
		Float i = Float.parseFloat(firstArg) * Float.parseFloat(secondArg);
		push(i + "");
	}
	private void divMethod() {
		String firstArg = pop();
		String secondArg = pop();
		Float i = Float.parseFloat(firstArg) / Float.parseFloat(secondArg);
		push(i + "");
	}
	private void minusMethod() {
		String firstArg = pop();
		String secondArg = pop();
		Float i = Float.parseFloat(firstArg) - Float.parseFloat(secondArg);
		push(i + "");
	}
	private void plusMethod() {
		String firstArg = pop();
		String secondArg = pop();
		Float i = Float.parseFloat(firstArg) + Float.parseFloat(secondArg);
		push(i + "");
	}

	private void stacktopMethod(String[] splitString) {
		String[] path = getPath(splitString[1]);
		if (!symbolTabelle.containsKey(path[0])) {
			symbolTabelle.put(path[0], new Symbol(lastLabel, pop()));
		} else {
			symbolTabelle.get(path[0]).setVariableValue(pop());
		}

	}

	private void labelMethod(String[] splitString) {
		if (splitString[1].contains("L"))
			lastLabel = splitString[1];
	}

	private void loadMethod(String[] splitString) {
		if (splitString[1].contains("idplace")) {
			String[] path = getPath(splitString[1]);
			Symbol s = symbolTabelle.get(path[0]);
			if (s == null)
				push("null");
			else if (path.length >= 3)
				push(s.getVariableValue());
		} else
			push(splitString[1]);
	}

	private void storeMethod(String[] splitString) {
		String[] path = getPath(splitString[1]);
		if (symbolTabelle.containsKey(path[0]))
			symbolTabelle.get(path[0]).setVariableValue(pop());
		else
			symbolTabelle.put(path[0], new Symbol(lastLabel, pop()));
	}

	private String[] getPath(String splitString) {
		return splitString.split("\\.");
	}

	private void push(String s) {
		log("pushed %s", s);
		stack.push(s);
		log(getStackAsString());
	}
	private String pop() {
		String s = stack.pop();
		log("popped %s", s);
		log(getStackAsString());
		return s;
	}

	private String getStackAsString() {
		String s = "(EOS";
		for (String entry : stack)
			s += " | " + entry;
		return s + ")";
	}

}
