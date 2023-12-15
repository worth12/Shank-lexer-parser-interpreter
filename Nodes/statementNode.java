package Nodes;
public class statementNode extends Node {// extends node abstract class

  private Node value; // value of the int

  public statementNode(Node value) { // constructor for int node
    this.value = value;
  }

  public String toString() { // overriden tostring to print node
    return "statement(" + this.value.toString() + ")";
  }

  public Node getValue() { // returns value of int node
    return value;
  }

  
}