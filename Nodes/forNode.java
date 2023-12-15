package Nodes;
import java.util.List;

public class forNode extends Node {

  private Node from;
  private Node to;
  private List<statementNode> statements;
  private variableReferenceNode variableInt;

  public forNode(variableReferenceNode variableInt  , Node from, Node to, List<statementNode> statements){
    this.from = from;
    this.to = to;
    this.statements = statements;
    this.variableInt = variableInt;
  }

  public variableReferenceNode getVariableInt() {
    return this.variableInt;
  }

  public Node getFrom() {
    return this.from;
  }

  public Node getTo() {
    return this.to;
  }

  public List<statementNode> getStatements() {
    return this.statements;
  }

  public String toString() {
    return "forNode(from(" + from.toString() + "), to(" + to.toString() + "), statements(" + statements.toString() + "))";
  }
  
}
