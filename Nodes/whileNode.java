package Nodes;
import java.util.List;

public class whileNode extends Node {

  private Node condition;
  private List<statementNode> statements;

  public whileNode(Node condition, List<statementNode> statements) {
    this.condition = condition;
    this.statements = statements;
  }

  public Node getCondition() {
    return this.condition;
  }

  public void updateCondition(booleanCompareNode condition){
    this.condition = condition;
  }

  public List<statementNode> getStatements() {
    return this.statements;
  }

  public String toString() {
    return "whileNode(condition(" + condition.toString() + "), statements(" + statements.toString() + "))";
  }
  
}
