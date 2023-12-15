package Nodes;
import java.util.List;

public class repeatNode extends Node{

  private Node condition;
  private List<statementNode> statements;

  
  public repeatNode(Node condition, List<statementNode> statements) {
    this.condition = condition;
    this.statements = statements;
  }

  public Node getCondition() {
    return this.condition;
  }

  public List<statementNode> getStatements() {
    return this.statements;
  }
  
  public String toString() {
    return "repeatNode(condition(" + condition.toString() + "), statements(" + statements.toString() + "))";
  }
  
}
