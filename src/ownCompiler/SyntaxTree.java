package ownCompiler;

import java.util.LinkedList;
import java.util.List;

public class SyntaxTree {

	private List<SyntaxTree> children ;
	private String value;
	private Function function;

	
	public SyntaxTree(Function function) {
		this.function = function;
		this.value=null;
		this.children = new LinkedList<>();
	}
	public SyntaxTree(Function function, String value) {
		this.function = function;
		this.value=value;
		this.children = new LinkedList<>();
	}
	public List<SyntaxTree> getChildren() {
		return children;
	}
	public void setChildren(List<SyntaxTree> children) {
		this.children = children;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
	}
	
	
}
