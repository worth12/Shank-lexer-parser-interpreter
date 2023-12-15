package Nodes;
public class assignmentNode extends statementNode {

	private variableReferenceNode varReference;

	private Node value;

	public assignmentNode(Node value, variableReferenceNode varReference) {
		super(value); // creates statement node with var reference and value
		this.varReference = varReference;
		this.value = value;
	}

	public variableReferenceNode getVariable() {
    return this.varReference;
  }

	public Node getValue() {
    return this.value;
  }

	public String toString() {
		return "assignment(" + varReference.toString() + ", " + value.toString() + ")";
	}

}
