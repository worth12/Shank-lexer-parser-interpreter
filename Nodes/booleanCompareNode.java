package Nodes;
public class booleanCompareNode extends Node {

	public enum NodeType { // enum for the different types of boolean operators
		OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL, NOTEQUALS, NOT
	}

	private NodeType nodeType;
	private Node left;
	private Node right;

	public booleanCompareNode(NodeType nodeType, Node left, Node right) { // node for boolean comparison, stores the left,
																																				// right, and comparitor
		this.nodeType = nodeType;
		this.left = left;
		this.right = right;
	}

	public booleanCompareNode.NodeType getNodeType() {
		return nodeType;
	}

	public Node getLeftNode() {
		return left;
	}

	public Node getRightNode() {
		return right;
	}

	public String toString() {
		return "booleanCompare(operator(" + this.nodeType + "),left(" + this.left.toString() + "),right("
				+ this.right.toString() + "))";
	}

}
