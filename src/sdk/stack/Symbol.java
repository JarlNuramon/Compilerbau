package sdk.stack;

class Symbol {
	private String label;
	private String variableValue;

	public Symbol(String label, String variableValue) {
		this.label = label;
		this.variableValue = variableValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

}
