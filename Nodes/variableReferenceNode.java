package Nodes;
public class variableReferenceNode extends Node {

	private String name;
	private Node index;

	public variableReferenceNode(String name, Node arrayIndex) { // node for when a variable is referenced, not defined
		this.name = name;
		this.index = arrayIndex;
	}

	public String toString() {
		if (this.index == null) {
			return "variableReferenceNode(name(" + name + "))";
		}
		return "variableReferenceNode(name(" + name + "),index(" + index.toString() + "))";
	}

	public Node getIndex() {
		return this.index;
	}

	public String getName() {
		return this.name;
	}

}
