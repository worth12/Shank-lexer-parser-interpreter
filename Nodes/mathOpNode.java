package Nodes;
public class mathOpNode extends Node {// extends node abstract class

  public enum operationType {
    PLUS, MINUS, TIMES, DIVIDE, MOD
  } // the states of operation types

  private Node leftNode, rightNode; // child nodes of operation node
  private operationType operationType; // type of operation

  public mathOpNode(operationType operationType, Node leftNode, Node rightNode) { // constructor
    this.leftNode = leftNode;
    this.rightNode = rightNode;
    this.operationType = operationType;
  }

  public Node getLeftNode() {
    return leftNode;
  }

  public Node getRightNode() {
    return rightNode;
  }

  public operationType getOperationType() {
    return operationType;
  }

  @Override
  public String toString() { // overriden tostring to print node
    String operationString = " ";
    switch (operationType) {
      case PLUS:
        operationString = "+";
        break;
      case MINUS:
        operationString = "-";
        break;
      case TIMES:
        operationString = "*";
        break;
      case DIVIDE:
        operationString = "/";
        break;
      case MOD:
        operationString = "%";
        break;
    }
    return "MathOpNode(" + operationString + "," + leftNode.toString() + "," + rightNode.toString() + ")";
  }

}
